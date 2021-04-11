package cz.novosadkry.VideoPlayer.Video;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;


public enum PixelColor {
    WHITE(Material.WHITE_WOOL),
    ORANGE(Material.ORANGE_WOOL),
    MAGENTA(Material.MAGENTA_WOOL),
    LIGHT_BLUE(Material.LIGHT_BLUE_WOOL),
    YELLOW(Material.YELLOW_WOOL),
    LIME(Material.LIME_WOOL),
    PINK(Material.PINK_WOOL),
    GRAY(Material.GRAY_WOOL),
    LIGHT_GRAY(Material.LIGHT_GRAY_WOOL),
    CYAN(Material.CYAN_WOOL),
    PURPLE(Material.PURPLE_WOOL),
    BLUE(Material.BLUE_WOOL),
    BROWN(Material.BROWN_WOOL),
    GREEN(Material.GREEN_WOOL),
    RED(Material.RED_WOOL),
    BLACK(Material.BLACK_WOOL);

    private static final HashMap<PixelColor, int[]> colors;

    static {
        colors = new HashMap<>();
        colors.put(WHITE, new int[] { 221, 221, 221 });
        colors.put(ORANGE, new int[] { 219, 125, 62 });
        colors.put(MAGENTA, new int[] { 179, 80, 188 });
        colors.put(LIGHT_BLUE, new int[] { 107, 138, 201 });
        colors.put(YELLOW, new int[] { 177, 168, 39 });
        colors.put(LIME, new int[] { 65, 174, 56 });
        colors.put(PINK, new int[] { 208, 132, 153 });
        colors.put(GRAY, new int[] { 64, 64, 64 });
        colors.put(LIGHT_GRAY, new int[] { 154, 161, 161 });
        colors.put(CYAN, new int[] { 46, 110, 137 });
        colors.put(PURPLE, new int[] { 126, 61, 181 });
        colors.put(BLUE, new int[] { 46, 56, 141 });
        colors.put(BROWN, new int[] { 79, 50, 31 });
        colors.put(GREEN, new int[] { 53, 70, 27 });
        colors.put(RED, new int[] { 150, 52, 48 });
        colors.put(BLACK, new int[] { 25, 25, 25 });
    }

    private final Material mat;

    PixelColor(Material mat) {
        this.mat = mat;
    }

    public Material toMaterial() {
        return mat;
    }

    public static PixelColor get(int[] p) {
        for (Map.Entry<PixelColor, int[]> entry : colors.entrySet()) {
            int[] range = entry.getValue();

            boolean match =
                approx(range[0], p[0], 35) &&
                approx(range[1], p[1], 35) &&
                approx(range[2], p[2], 35);

            if (match)
                return entry.getKey();
        }

        int v = avg(p);

        if (v > 180)
            return WHITE;
        else if (v > 60)
            return GRAY;
        else
            return BLACK;
    }

    private static int avg(int[] p) {
        return (p[0] + p[1] + p[2]) / (255 * 3);
    }

    private static boolean approx(int range, int value, int delta) {
        return (value > range - delta) && (value < range + delta);
    }
}
