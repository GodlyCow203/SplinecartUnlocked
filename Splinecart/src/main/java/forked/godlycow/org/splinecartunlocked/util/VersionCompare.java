package forked.godlycow.org.splinecartunlocked.util;

import java.util.ArrayList;
import java.util.List;

public final class VersionCompare {
    private VersionCompare() {
    }

    /** Compares two version strings like "1.2.3", "1.2.3+26.2" or "1.2.3-beta.1". Returns <0, 0 or >0. */
    public static int compare(String a, String b) {
        List<Integer> pa = parse(a);
        List<Integer> pb = parse(b);
        int n = Math.max(pa.size(), pb.size());
        for (int i = 0; i < n; i++) {
            int x = i < pa.size() ? pa.get(i) : 0;
            int y = i < pb.size() ? pb.get(i) : 0;
            if (x != y) {
                return Integer.compare(x, y);
            }
        }
        return 0;
    }

    private static List<Integer> parse(String version) {
        List<Integer> parts = new ArrayList<>();
        for (String part : version.split("[.+-]")) {
            int value = 0;
            StringBuilder digits = new StringBuilder();
            for (int i = 0; i < part.length(); i++) {
                char c = part.charAt(i);
                if (c >= '0' && c <= '9') {
                    digits.append(c);
                } else if (!digits.isEmpty()) {
                    break;
                }
            }
            if (!digits.isEmpty()) {
                try {
                    value = Integer.parseInt(digits.toString());
                } catch (NumberFormatException ignored) {
                    value = Integer.MAX_VALUE;
                }
            }
            parts.add(value);
        }
        return parts;
    }
}