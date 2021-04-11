package cz.novosadkry.VideoPlayer.Commands;

import cz.novosadkry.VideoPlayer.Video.VideoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VideoPlayerExecutor implements CommandExecutor {
    private static VideoPlayer video;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        switch (args[0]) {
            case "create":
                if (video == null) {
                    video = new VideoPlayer(args[1], player.getLocation(), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                    video.build();
                }
                else
                    player.sendMessage("Destroy previous player first!");
                break;

            case "destroy":
                if (video != null) {
                    video.destroy();
                    video = null;
                } else
                    player.sendMessage("You need to create a player first!");
                break;

            case "stop":
                if (video != null)
                    video.stop();
                else
                    player.sendMessage("You need to create a player first!");
                break;

            case "play":
                if (video != null)
                    video.play();
                else
                    player.sendMessage("You need to create a player first!");
                break;
        }

        return true;
    }
}
