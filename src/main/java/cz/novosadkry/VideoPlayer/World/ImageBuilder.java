package cz.novosadkry.VideoPlayer.World;

import cz.novosadkry.VideoPlayer.Video.VideoMode;
import org.bukkit.Location;

import java.awt.image.BufferedImage;

public abstract class ImageBuilder {
    protected final Location start;
    protected final int width;
    protected final int height;
    protected VideoMode mode;

    public ImageBuilder(Location start, int width, int height) {
        this(start, width, height, VideoMode.RGB);
    }

    public ImageBuilder(Location start, int width, int height, VideoMode mode) {
        this.mode = mode;
        this.start = start;
        this.width = width;
        this.height = height;
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

    public abstract void destroy();

    public abstract void build();

    public abstract void build(BufferedImage image);
}
