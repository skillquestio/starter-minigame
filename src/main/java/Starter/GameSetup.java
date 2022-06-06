package Starter;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.skillquest.mgapi.MinigamePlugin;
import io.skillquest.mgapi.SetupCommand;

public class GameSetup extends SetupCommand {

  public GameSetup(MinigamePlugin plugin) {
    super(plugin);
  }

  @Override
  public void showHelp(Player player) {
    // Put whatever you want in here!
    player.sendMessage(ChatColor.YELLOW + "setspectatorspawn - Sets the spawnpoint for new spectators");
    player.sendMessage(ChatColor.YELLOW + "<cmd> - <description>");
  }

  @Override
  public void executeCommand(Player player, String command, List<String> args) {
    if (command.equalsIgnoreCase("setspectatorspawn")) {
      if (getPlugin().getWorldManager().getMasterWorld() == null) {
        player.sendMessage(ChatColor.RED
            + "The master world doesn't exist yet! Type \"generate\" or \"setmasterworld\" in chat to set the master world.");
      } else if (!player.getWorld().getName().equals(getPlugin().getWorldManager().getMasterWorld())) {
        player.sendMessage(ChatColor.RED
            + "You aren't in the master world! Type \"teleport\" in chat to teleport to the master world.");
      } else {
        getPlugin().getConfig().set("spectator.x", player.getLocation().getX());
        getPlugin().getConfig().set("spectator.y", player.getLocation().getY());
        getPlugin().getConfig().set("spectator.z", player.getLocation().getZ());
        getPlugin().getConfig().set("spectator.yaw", player.getLocation().getYaw());
        getPlugin().getConfig().set("spectator.pitch", player.getLocation().getPitch());
        getPlugin().saveConfig();

        player.sendMessage(ChatColor.GREEN + "Spectator spawn set!");
      }
    } else if (command.equalsIgnoreCase("your_setup_command")) {
      // Code here
    }
  }

}
