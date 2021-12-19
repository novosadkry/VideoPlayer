package cz.novosadkry.VideoPlayer.Video;

import cz.novosadkry.VideoPlayer.Main;
import cz.novosadkry.VideoPlayer.World.BlockImageBuilder;
import cz.novosadkry.VideoPlayer.World.ImageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;

public class VideoPlayer {
    private final String file;

    private VideoMode mode;
    private boolean initialized;
    private boolean playing;
    private BukkitTask task;

    private FrameGrabber frameGrabber;
    private final ImageBuilder frameBuilder;
    private final Java2DFrameConverter frameConverter;

    public VideoPlayer(String file, ImageBuilder builder) {
        this(file, builder, VideoMode.RGB);
    }

    public VideoPlayer(String file, ImageBuilder builder, VideoMode mode) {
        this.file = file;
        this.mode = mode;
        this.frameBuilder = builder;
        this.frameConverter = new Java2DFrameConverter();
    }

    public void build() {
        frameBuilder.build();
    }

    public void destroy() {
        frameBuilder.destroy();
        stop();
    }

    private void initialize() {
        frameGrabber = new OpenCVFrameGrabber(file);
        frameGrabber.setAudioChannels(0);
        frameGrabber.setImageWidth(frameBuilder.getWidth());
        frameGrabber.setImageHeight(frameBuilder.getHeight());

        try {
            frameGrabber.start();

            task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
                if (!playing)
                    return;

                BufferedImage image = null;
                try { image = frameConverter.convert(frameGrabber.grab()); }
                catch (Exception e) { e.printStackTrace(); }

                if (image != null) {
                    frameBuilder.build(image);
                } else stop();
            }, 0L, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialized = true;
    }

    public void play() {
        if (!initialized)
            initialize();

        playing = true;
    }

    public void pause() {
        playing = false;
    }

    public void stop() {
        if (task != null)
            task.cancel();

        try { frameGrabber.stop(); }
        catch (FrameGrabber.Exception e) { e.printStackTrace(); }

        initialized = false;
    }

    public VideoMode getMode() {
        return mode;
    }

    public void setMode(VideoMode mode) {
        this.mode = mode;
        this.frameBuilder.setMode(mode);
    }
}
