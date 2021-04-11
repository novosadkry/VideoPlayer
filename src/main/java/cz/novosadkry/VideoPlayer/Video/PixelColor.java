package cz.novosadkry.VideoPlayer.Video;

import org.bukkit.Material;

public enum PixelColor {
    WHITE(Material.WHITE_WOOL),
    GRAY(Material.GRAY_WOOL),
    BLACK(Material.BLACK_WOOL);

    private final Material mat;

    PixelColor(Material mat) {
        this.mat = mat;
    }

    public static PixelColor get(int[] p) {
        if (p[0] > 180)
            return WHITE;

        else if (p[0] > 60)
            return GRAY;

        else
            return BLACK;
    }

    public Material toMaterial() {
        return mat;
    }
}
