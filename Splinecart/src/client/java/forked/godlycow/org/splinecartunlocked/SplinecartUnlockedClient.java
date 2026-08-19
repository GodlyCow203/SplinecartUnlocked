package forked.godlycow.org.splinecartunlocked;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import forked.godlycow.org.splinecartunlocked.block.TrackGeometry;
import forked.godlycow.org.splinecartunlocked.block.entity.ClientTrackGeometry;
import forked.godlycow.org.splinecartunlocked.block.entity.TrackTiesBlockEntityRenderer;
import forked.godlycow.org.splinecartunlocked.config.Config;
import forked.godlycow.org.splinecartunlocked.config.ConfigOption;
import forked.godlycow.org.splinecartunlocked.util.SUtil;
import forked.godlycow.org.splinecartunlocked.util.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;

import java.io.IOException;

public class SplinecartUnlockedClient implements ClientModInitializer {
	public static final Config CONFIG = new Config("splinecartunlocked_client",
			() -> FabricLoader.getInstance().getConfigDir()
					.resolve("splinecartunlocked").resolve("splinecartunlocked_client.properties"));

	public static final ConfigOption.BooleanOption CFG_ROTATE_CAMERA = CONFIG.optBool("rotate_camera", true);
	public static final ConfigOption.BooleanOption CFG_VBOS = CONFIG.optBool("vbos", false);
	public static final ConfigOption.IntOption CFG_TRACK_RESOLUTION = CONFIG.optInt("track_resolution", 3, 1, 16);
	public static final ConfigOption.IntOption CFG_TRACK_RENDER_DISTANCE = CONFIG.optInt("track_render_distance", 8, 4, 32);
	public static final ConfigOption.BooleanOption CFG_NOTIFY_UPDATES = CONFIG.optBool("notify_updates", true);

	@Override
	public void onInitializeClient() {
		SUtil.TICK_DELTA = () -> Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

		try {
			CONFIG.load();
		} catch (IOException e) {
			SplinecartUnlocked.LOGGER.error("Error loading client config on mod init", e);
		}

		BlockEntityRendererRegistry.register(SplinecartUnlocked.TRACK_TIES_BE, TrackTiesBlockEntityRenderer::new);
		EntityRendererRegistry.register(SplinecartUnlocked.TRACK_FOLLOWER, NoopRenderer::new);

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(
					LiteralArgumentBuilder.<FabricClientCommandSource>literal("splinecartc")
							.then(CONFIG.command(LiteralArgumentBuilder.<FabricClientCommandSource>literal("config"),
									FabricClientCommandSource::sendFeedback))
							.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("update")
									.executes(context -> {
										UpdateChecker.checkNow(context.getSource());
										return 1;
									}))
		));

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
				UpdateChecker.checkOnJoin());

		HudElementRegistry.addLast(SplinecartUnlocked.id("hud"), new SplinecartUnlockedHud());
		TrackGeometry.CONSTRUCTOR = ClientTrackGeometry::new;
	}
}
