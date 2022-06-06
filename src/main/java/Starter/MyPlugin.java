package Starter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import io.skillquest.mgapi.MinigamePlugin;

/**
 * The main class for our plugin, as specified in src/resources/plugin.yml.
 */
public class MyPlugin extends MinigamePlugin implements Listener {

  public static MyPlugin pl;
  public static PluginManager pm;

  // No modification needed
  public void onEnable() {
    super.onEnable();
    pl = this;
    pm = Bukkit.getPluginManager();

    pm.registerEvents(this, this);

    new GameSetup(this);
  }

  // No modification needed
  public void onDisable() {
    super.onDisable();
  }

  // No modification needed
  @Override
  public boolean createGame() {
    World world = getWorldManager().createGameWorld();
    if (world == null)
      return false;

    Minigame mg = new Minigame(this, world);
    getGameManager().initialize(mg);
    Bukkit.getPluginManager().registerEvents(mg, this);
    return true;
  }

  @Override
  public String getMinigameDescription() {
    // Change
    return ChatColor.WHITE + "Put your minigame description here";
  }

  @Override
  public Material getMinigameMaterial() {
    // Change
    return Material.SCAFFOLDING;
  }

  @Override
  public String getMinigameName() {
    // Change
    return ChatColor.GREEN + ChatColor.BOLD.toString() + "Minigame Name";
  }

  @Override
  public List<String> getOwnerUUIDs() {
    // Change
    return Arrays.asList("22559e3e-f089-4fc3-b669-fcd3c8392a77", "0ad72b9a-dc26-480e-8d14-d9a760aa94da");
  }

  @Override
  public String getAuthors() {
    // Change
    return "Name here";
  }
}