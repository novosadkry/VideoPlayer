package cz.novosadkry.VideoPlayer;

import cz.novosadkry.VideoPlayer.Commands.VideoPlayerExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("vp").setExecutor(new VideoPlayerExecutor());
    }
}
