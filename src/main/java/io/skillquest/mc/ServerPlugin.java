package io.skillquest.mc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.skillquest.mc.Util.ClickableInventory;

/**
 * The main class for our plugin, as specified in src/resources/plugin.yml.
 */
public class ServerPlugin extends JavaPlugin implements Listener {

  public static Plugin pl;
  public static PluginManager pm;

  public void onEnable() {
    pl = this;
    pm = Bukkit.getPluginManager();

    pm.registerEvents(this, this);

    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      @Override
      public void run() {
        // Clickable Inventory Refresh
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() instanceof ClickableInventory) {
            ClickableInventory ci = (ClickableInventory) p.getOpenInventory().getTopInventory();
            ci.refresh();
          }
        }
      }
    }, 0L, 20L);
  }

  /**
   * Provided. This function gives ClickableInventories their functionality by
   * listening in on the InventoryClickEvent.
   * 
   * @param e An inventory click event
   */
  @EventHandler
  public void onInvClick(InventoryClickEvent e) {
    if (e.getInventory() instanceof ClickableInventory) {
      ((ClickableInventory) e.getInventory()).onClick(e);
    }
  }
}