package cz.novosadkry.VideoPlayer;

import cz.novosadkry.VideoPlayer.Commands.VideoPlayerExecutor;
import cz.novosadkry.VideoPlayer.Commands.VideoPlayerTabCompleter;
import org.bukkit.map.MapPalette;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static final String VIDEO_FOLDER = "./plugins/VideoPlayer/";
    public static byte[] cachedColorIds = new byte[256 * 256 * 256];

    @Override
    public void onEnable() {
        getCommand("vp").setExecutor(new VideoPlayerExecutor());
        getCommand("vp").setTabCompleter(new VideoPlayerTabCompleter());

        getLogger().info("Generating colors...");
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    int i = ((r & 0xFF) | ((g & 0xFF) << 8) | ((b & 0xFF) << 16));
                    cachedColorIds[i] = MapPalette.matchColor(r, g, b);
                }
            }
        }
    }
}
