package forked.godlycow.org.splinecartunlocked.block.entity;

import forked.godlycow.org.splinecartunlocked.block.TrackGeometry;
import forked.godlycow.org.splinecartunlocked.block.TrackTiesBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;

public class ClientTrackGeometry extends TrackGeometry {
    public ClientTrackGeometry(TrackTiesBlockEntity trackTies) {
        super(trackTies);
    }

    public boolean render(PoseStack matrices,
                          int light, int overlay, int segs, float olVOffset, Vector3fc olColor,
                          int powerState, int trackResolution,
                          Identifier trackTexture, Identifier overlayTexture,
                          TrackTiesBlockEntity curr, TrackTiesBlockEntity prevE, TrackTiesBlockEntity nextE) {
        return false;
    }

    @Override
    public void close() {
        super.close();
    }
}
