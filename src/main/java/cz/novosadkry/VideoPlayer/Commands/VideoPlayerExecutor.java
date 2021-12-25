package cz.novosadkry.VideoPlayer.Commands;

import cz.novosadkry.VideoPlayer.Main;
import cz.novosadkry.VideoPlayer.Video.VideoMode;
import cz.novosadkry.VideoPlayer.Video.VideoPlayer;
import cz.novosadkry.VideoPlayer.World.BlockImageBuilder;
import cz.novosadkry.VideoPlayer.World.MapImageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VideoPlayerExecutor implements CommandExecutor {
    private static VideoPlayer current;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        switch (args[0]) {
            case "create":
                if (current == null) {
                    current = new VideoPlayer(
                        Main.VIDEO_FOLDER + args[1],
                        new MapImageBuilder(
                            player.getLocation(),
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3])
                        )
                    );

                    current.build();
                }
                else
                    player.sendMessage("Destroy previous player first!");
                break;

            case "destroy":
                if (current != null) {
                    current.destroy();
                    current = null;
                } else
                    player.sendMessage("You need to create a player first!");
                break;

            case "stop":
                if (current != null)
                    current.stop();
                else
                    player.sendMessage("You need to create a player first!");
                break;

            case "pause":
                if (current != null)
                    current.pause();
                else
                    player.sendMessage("You need to create a player first!");

            case "play":
                if (current != null)
                    current.play();
                else
                    player.sendMessage("You need to create a player first!");
                break;

            case "mode":
                if (current != null)
                    current.setMode(VideoMode.valueOf(args[1]));
                else
                    player.sendMessage("You need to create a player first!");
                break;
        }

        return true;
    }
}
