package cz.novosadkry.VideoPlayer;

import cz.novosadkry.VideoPlayer.Commands.VideoPlayerExecutor;
import cz.novosadkry.VideoPlayer.Commands.VideoPlayerTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static final String VIDEO_FOLDER = "./plugins/VideoPlayer/";

    @Override
    public void onEnable() {
        getCommand("vp").setExecutor(new VideoPlayerExecutor());
        getCommand("vp").setTabCompleter(new VideoPlayerTabCompleter());
    }
}
