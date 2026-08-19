package forked.godlycow.org.splinecartunlocked.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class ChatUtil {
    public static final int BRAND = 0xB07939;
    public static final int BRACKET = 0x9BA3AC;
    public static final int TEXT = 0xE8E4DD;
    public static final int GOOD = 0x8FBF6B;
    public static final int WARN = 0xE3A94C;
    public static final int ERROR = 0xD96A5F;
    public static final int DIM = 0x9C968D;

    private ChatUtil() {
    }

    public static MutableComponent prefix() {
        return Component.literal("[").withColor(BRACKET)
                .append(Component.literal("Splinecart").withColor(BRAND))
                .append(Component.literal("] ").withColor(BRACKET));
    }

    public static MutableComponent prefixed(Component message) {
        return prefix().append(message);
    }

    public static MutableComponent styled(Component message, int color) {
        return message.copy().withColor(color);
    }

    public static MutableComponent literal(String text, int color) {
        return Component.literal(text).withColor(color);
    }
}