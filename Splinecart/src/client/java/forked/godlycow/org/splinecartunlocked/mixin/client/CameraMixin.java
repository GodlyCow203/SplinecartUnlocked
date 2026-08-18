package forked.godlycow.org.splinecartunlocked.mixin.client;

import forked.godlycow.org.splinecartunlocked.SplinecartUnlockedClient;
import forked.godlycow.org.splinecartunlocked.entity.TrackFollowerEntity;
import forked.godlycow.org.splinecartunlocked.util.SUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique private static final int BLEND_FRAMES = 12;
    @Unique private static final Quaternionf IDENTITY = new Quaternionf();
    // bool so we can detect the exact moment we start/stop riding and kick off a blend
    @Unique private boolean splinecartunlocked$wasRiding = false;
    // frames left in the current blend, 0 means no blend running
    @Unique private int splinecartunlocked$blendFrames = 0;
    // rotation we captured when the blend started
    @Unique private final Quaternionf splinecartunlocked$blendStart = new Quaternionf();
    // the lerped/blended result we actually apply
    @Unique private final Quaternionf splinecartunlocked$blendRot = new Quaternionf();

    @Shadow protected abstract void setPosition(Vec3 pos);
    @Shadow @Final private Quaternionf rotation;
    @Shadow private Entity entity;
    @Shadow private net.minecraft.world.level.Level level;

    @Inject(method = "alignWithEntity(F)V", at = @At("TAIL"))
    private void splinecartunlocked$updateCamPosWhileRiding(float tickDelta, CallbackInfo info) {
        var self = this.entity;
        if (self != null) {
            var vehicle = self.getVehicle();
            if (vehicle != null) {
                var tf = vehicle.getVehicle();
                if (tf instanceof TrackFollowerEntity trackFollower) {
                    var world = this.level;
                    var diff = self.position().add(0, self.getEyeHeight(), 0).subtract(trackFollower.position());
                    var camPos = new Vector3d(diff.x, diff.y, diff.z);
                    if (world.isClientSide()) {
                        var rot = new Quaternionf();
                        trackFollower.getClientOrientation(rot, tickDelta);
                        rot.transform(camPos);

                        if (SUtil.failsSanityCheck(camPos)) {
                            return;
                        }

                        this.setPosition(new Vec3(camPos.x, camPos.y, camPos.z).add(trackFollower.getLerpedPosition(tickDelta)));
                    }
                }
            }
        }
    }

    @Inject(method = "setRotation(FF)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 0, target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;", remap = false))
    private void splinecartunlocked$updateCamRotationWhileRiding(float yaw, float pitch, CallbackInfo info) {
        var self = this.entity;
        if (self == null || !self.level().isClientSide()) {
            return;
        }

        var vehicle = self.getVehicle();
        var tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

        if (vehicle != null && vehicle.getVehicle() instanceof TrackFollowerEntity trackFollower && SplinecartUnlockedClient.CFG_ROTATE_CAMERA.get()) {
            var rot = new Quaternionf();
            trackFollower.getClientOrientation(rot, tickDelta);
            if (SUtil.failsSanityCheck(rot)) {
                return;
            }

            var full = new Quaternionf(rot).mul(new Quaternionf().rotationY(Mth.DEG_TO_RAD * (90 + vehicle.getViewYRot(tickDelta))));

            //just got on the track: start blend from identity (= normal pre-ride view) so cam dont snap to track rot
            if (!this.splinecartunlocked$wasRiding) {
                this.splinecartunlocked$wasRiding = true;
                this.splinecartunlocked$blendFrames = BLEND_FRAMES;
                this.splinecartunlocked$blendStart.identity();
            }

            if (this.splinecartunlocked$blendFrames > 0) {
                // animate slerp over a few frames, progress goes 0->1 so it starts at the old view
                float progress = 1f - (float) (this.splinecartunlocked$blendFrames - 1) / BLEND_FRAMES;
                this.splinecartunlocked$blendStart.slerp(full, progress, this.splinecartunlocked$blendRot);
                this.splinecartunlocked$blendFrames--;
            } else {
                //blend done, just use track rot directly
                this.splinecartunlocked$blendRot.set(full);
            }

            if (SUtil.failsSanityCheck(this.splinecartunlocked$blendRot)) {
                return;
            }

            this.splinecartunlocked$blendRot.mul(rotation, rotation);
        } else {
            // left the track, ease rot back to identity so we dont snap back to vanilla cam view
            if (this.splinecartunlocked$wasRiding) {
                this.splinecartunlocked$wasRiding = false;
                this.splinecartunlocked$blendFrames = BLEND_FRAMES;
                this.splinecartunlocked$blendStart.set(this.splinecartunlocked$blendRot);
            }

            if (this.splinecartunlocked$blendFrames > 0) {
                float progress = 1f - (float) (this.splinecartunlocked$blendFrames - 1) / BLEND_FRAMES;
                this.splinecartunlocked$blendStart.slerp(IDENTITY, progress, this.splinecartunlocked$blendRot);

                if (!SUtil.failsSanityCheck(this.splinecartunlocked$blendRot)) {
                    this.splinecartunlocked$blendRot.mul(rotation, rotation);
                }

                this.splinecartunlocked$blendFrames--;
            }
        }
    }
}
