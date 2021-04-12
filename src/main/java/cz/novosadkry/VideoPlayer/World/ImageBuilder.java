package cz.novosadkry.VideoPlayer.World;

import cz.novosadkry.VideoPlayer.Video.PixelColor;
import cz.novosadkry.VideoPlayer.Video.VideoMode;
import org.bukkit.Location;
import org.bukkit.Material;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageBuilder {
    private VideoMode mode;
    private final Location start;
    private final int width, height;

    public ImageBuilder(Location start, int width, int height) {
        this(start, width, height, VideoMode.RGB);
    }

    public ImageBuilder(Location start, int width, int height, VideoMode mode) {
        this.start = start;
        this.width = width;
        this.height = height;
        this.mode = mode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public VideoMode getMode() {
        return mode;
    }

    public void setMode(VideoMode mode) {
        this.mode = mode;
    }

    public void destroy() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.AIR, false);
            }
        }
    }

    public void build() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(PixelColor.BLACK.toMaterial(), false);
            }
        }
    }

    public void build(BufferedImage image) {
        Raster data = image.getData();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] p = data.getPixel(width - x - 1, height - y - 1, (int[]) null);
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(PixelColor.get(p, mode).toMaterial(), false);
            }
        }
    }
}
