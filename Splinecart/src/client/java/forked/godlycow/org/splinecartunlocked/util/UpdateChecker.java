package forked.godlycow.org.splinecartunlocked.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import forked.godlycow.org.splinecartunlocked.SplinecartUnlocked;
import forked.godlycow.org.splinecartunlocked.SplinecartUnlockedClient;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class UpdateChecker {
	public static final String MODRINTH_PROJECT = "BFEpFp45";
	private static final String VERSION_API = "https://api.modrinth.com/v2/project/" + MODRINTH_PROJECT + "/version";
	private static final String MODRINTH_URL = "https://modrinth.com/mod/" + MODRINTH_PROJECT;

	private static final HttpClient HTTP = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(5))
			.followRedirects(HttpClient.Redirect.NORMAL)
			.build();

	private UpdateChecker() {
	}

	public static void checkOnJoin() {
		check(message -> {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && message != null) {
				player.sendSystemMessage(message);
			}
		}, !SplinecartUnlockedClient.CFG_NOTIFY_UPDATES.get(), true);
	}

	public static void checkNow(FabricClientCommandSource source) {
		check(message -> {
			if (message != null) {
				source.sendFeedback(message);
			}
		}, false, false);
	}

	private static void check(MessageSink sink, boolean silentIfUpToDate, boolean silentOnFailure) {
		Minecraft client = Minecraft.getInstance();
		HttpRequest request = request();
		if (request == null) {
			return;
		}

		HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApplyAsync(UpdateChecker::parseLatest, client)
				.thenAcceptAsync(remote -> sink.accept(buildMessage(remote, silentIfUpToDate, silentOnFailure)), client);
	}

	private interface MessageSink {
		void accept(Component message);
	}

	private static HttpRequest request() {
		try {
			return HttpRequest.newBuilder(URI.create(VERSION_API))
					.timeout(Duration.ofSeconds(10))
					.header("User-Agent", "SplinecartUnlocked/" + currentVersion())
					.GET()
					.build();
		} catch (Exception e) {
			SplinecartUnlocked.LOGGER.warn("Failed to build update check request", e);
			return null;
		}
	}

	private static String parseLatest(HttpResponse<String> response) {
		if (response.statusCode() != 200) {
			return null;
		}

		try {
			String minecraftVersion = Minecraft.getInstance().getLaunchedVersion();
			String best = null;

			for (JsonElement element : JsonParser.parseString(response.body()).getAsJsonArray()) {
				var version = element.getAsJsonObject();

				boolean fabric = false;
				for (JsonElement loader : version.getAsJsonArray("loaders")) {
					if ("fabric".equals(loader.getAsString())) {
						fabric = true;
						break;
					}
				}

				boolean matchesGame = false;
				for (JsonElement game : version.getAsJsonArray("game_versions")) {
					if (minecraftVersion.equals(game.getAsString())) {
						matchesGame = true;
						break;
					}
				}

				if (!fabric || !matchesGame) {
					continue;
				}

				String candidate = version.get("version_number").getAsString();
				if (best == null || VersionCompare.compare(candidate, best) > 0) {
					best = candidate;
				}
			}

			return best;
		} catch (Exception e) {
			SplinecartUnlocked.LOGGER.warn("Failed to parse modrinth update response", e);
			return null;
		}
	}

	private static Component buildMessage(String remote, boolean silentIfUpToDate, boolean silentOnFailure) {
		String local = currentVersion();

		if (remote == null) {
			if (silentIfUpToDate || silentOnFailure) {
				return null;
			}
			return ChatUtil.prefixed(ChatUtil.styled(
					Component.translatable("splinecartunlocked.update.error"), ChatUtil.DIM));
		}

		int compared = VersionCompare.compare(remote, local);
		if (compared <= 0) {
			if (silentIfUpToDate) {
				return null;
			}
			return ChatUtil.prefixed(ChatUtil.styled(
					Component.translatable("splinecartunlocked.update.up_to_date", local), ChatUtil.GOOD));
		}

		return ChatUtil.prefixed(ChatUtil.styled(
				Component.translatable("splinecartunlocked.update.outdated", remote, local), ChatUtil.WARN)
				.append(Component.literal(" "))
				.append(Component.literal("[" + "Modrinth" + "]")
						.withStyle(Style.EMPTY
								.withColor(ChatUtil.BRAND)
								.withUnderlined(true)
								.withClickEvent(new ClickEvent.OpenUrl(URI.create(MODRINTH_URL))))));
	}

	public static String currentVersion() {
		return FabricLoader.getInstance().getModContainer(SplinecartUnlocked.MOD_ID)
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
	}
}