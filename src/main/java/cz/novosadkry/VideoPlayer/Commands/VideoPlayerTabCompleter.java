package cz.novosadkry.VideoPlayer.Commands;

import cz.novosadkry.VideoPlayer.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VideoPlayerTabCompleter implements TabCompleter {
    private final List<String> availableCommands = Arrays.asList(
            "create",
            "destroy",
            "stop",
            "pause",
            "play"
    );

    public List<String> getFolderContents(File folder) {
        if (!folder.isDirectory())
            return null;

        File[] files = folder.listFiles();

        if (files == null)
            return null;

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAvailableVideos() {
        File vpFolder = new File(Main.VIDEO_FOLDER);

        if (!vpFolder.exists())
            vpFolder.mkdirs();

        return getFolderContents(vpFolder);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 1) {
            return availableCommands.stream()
                    .filter(c -> c.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                return getAvailableVideos().stream()
                        .filter(v -> v.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }

        return null;
    }
}
