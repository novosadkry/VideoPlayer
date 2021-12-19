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

    // Palettes
    private static final HashMap<PixelColor, int[]> rgb;
    private static final HashMap<PixelColor, int[]> grayScale;

    static {
        rgb = new HashMap<>();
        rgb.put(WHITE, new int[] { 221, 221, 221 });
        rgb.put(ORANGE, new int[] { 219, 125, 62 });
        rgb.put(MAGENTA, new int[] { 179, 80, 188 });
        rgb.put(LIGHT_BLUE, new int[] { 107, 138, 201 });
        rgb.put(YELLOW, new int[] { 177, 168, 39 });
        rgb.put(LIME, new int[] { 65, 174, 56 });
        rgb.put(PINK, new int[] { 208, 132, 153 });
        rgb.put(GRAY, new int[] { 64, 64, 64 });
        rgb.put(LIGHT_GRAY, new int[] { 154, 161, 161 });
        rgb.put(CYAN, new int[] { 46, 110, 137 });
        rgb.put(PURPLE, new int[] { 126, 61, 181 });
        rgb.put(BLUE, new int[] { 46, 56, 141 });
        rgb.put(BROWN, new int[] { 79, 50, 31 });
        rgb.put(GREEN, new int[] { 53, 70, 27 });
        rgb.put(RED, new int[] { 150, 52, 48 });
        rgb.put(BLACK, new int[] { 25, 25, 25 });

        grayScale = new HashMap<>();
        grayScale.put(WHITE, new int[] { 255, 255, 255 });
        grayScale.put(LIGHT_GRAY, new int[] { 180, 180, 180 });
        grayScale.put(GRAY, new int[] { 60, 60, 60 });
        grayScale.put(BLACK, new int[] { 0, 0, 0 });
    }

    private final Material mat;

    PixelColor(Material mat) {
        this.mat = mat;
    }

    public Material toMaterial() {
        return mat;
    }

    public static PixelColor get(int[] p, VideoMode mode) {
        switch (mode) {
            case RGB:
                return getRGB(p);
            case GRAYSCALE:
                return getGrayScale(p);
            default:
                return BLACK;
        }
    }

    public static PixelColor getGrayScale(int[] p) {
        PixelColor out = BLACK;
        int closest = Integer.MAX_VALUE;

        for (Map.Entry<PixelColor, int[]> entry : grayScale.entrySet()) {
            int[] paletteColor = entry.getValue();
            int distance = distance(paletteColor, p);

            if (distance < closest) {
                closest = distance;
                out = entry.getKey();
            }
        }

        return out;
    }

    public static PixelColor getRGB(int[] p) {
        PixelColor out = BLACK;
        int closest = Integer.MAX_VALUE;

        for (Map.Entry<PixelColor, int[]> entry : rgb.entrySet()) {
            int[] paletteColor = entry.getValue();
            int distance = distance(paletteColor, p);

            if (distance < closest) {
                closest = distance;
                out = entry.getKey();
            }
        }

        return out;
    }

    private static int distance(int[] p1, int[] p2) {
        int r = p1[0] - p2[0];
        int g = p1[1] - p2[1];
        int b = p1[2] - p2[2];

        return (r * r) + (g * g) + (b * b);
    }
}
