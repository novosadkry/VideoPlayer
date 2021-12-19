package cz.novosadkry.VideoPlayer.World;

import cz.novosadkry.VideoPlayer.Video.PixelColor;
import cz.novosadkry.VideoPlayer.Video.VideoMode;
import org.bukkit.Location;
import org.bukkit.Material;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class BlockImageBuilder extends ImageBuilder {

    public BlockImageBuilder(Location start, int width, int height) {
        super(start, width, height);
    }

    public BlockImageBuilder(Location start, int width, int height, VideoMode mode) {
        super(start, width, height, mode);
    }

    @Override
    public void destroy() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.AIR, false);
            }
        }
    }

    @Override
    public void build() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(PixelColor.BLACK.toMaterial(), false);
            }
        }
    }

    @Override
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
