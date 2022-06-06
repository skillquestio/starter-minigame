package Starter;

import java.util.ArrayList;
import java.util.List;

import com.coloredcarrot.api.sidebar.SidebarString;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import io.skillquest.mgapi.Game;
import io.skillquest.mgapi.GameStatus;
import io.skillquest.mgapi.MinigamePlugin;

/**
 * Change anything and everything you want in here to make your minigame the
 * greatest!
 */
public class Minigame extends Game {

  private List<Player> spectators, alive;
  private boolean complete;

  public Minigame(MinigamePlugin plugin, World world) {
    super(plugin, world, ChatColor.GREEN + ChatColor.BOLD.toString() + "My Minigame");

    this.spectators = new ArrayList<>();
    this.alive = new ArrayList<>();
    this.complete = false;

    setCanPlayersJoinMidgame(true);
    setCollision(Team.OptionStatus.ALWAYS);
    setNametagsVisible(Team.OptionStatus.NEVER);
  }

  @Override
  public void applyPlayer(Player p) {
    /**
     * If you want to set custom properties on your alive players during the game,
     * do those here!
     */
    p.setTotalExperience(0);
    p.setExp(0);
    p.setLevel(0);
    p.getInventory().clear();
    p.updateInventory();
    p.setHealth(20);
    p.setHealthScaled(false);
    p.setSaturation(20);
    p.setInvulnerable(false);
    p.setAllowFlight(false);
    p.setFlying(false);
    p.setFoodLevel(20);
    p.setArrowsInBody(0);
    p.setCanPickupItems(true);
    p.setCustomName(null);
    p.setGameMode(GameMode.SURVIVAL);
    p.setGlowing(false);
    p.setSilent(false);

    // Hide spectators from the players
    for (Player pl : spectators) {
      if (!pl.getName().equals(p.getName())) {
        p.hidePlayer(getPlugin(), pl);
      }
    }
  }

  @Override
  public void onInitialize() {}

  @Override
  public void onFinish() {}

  @Override
  public void onGameStart() {
    /*
     * This is where your game officially begins! Teleport players to spawn
     * locations and, if your game requires it, add your own countdown to officially
     * kick off the game
     */
    for (Player p : getPlayers()) {
      alive.add(p);
      p.teleport(getWorld().getSpawnLocation());
      applyPlayer(p);
    }
  }

  private void gameOver(Player winner) {
    if (this.complete)
      return;

    this.complete = true;

    // You can change the game over stuff here if you want
    getPlayers().forEach(x -> {
      x.sendTitle(ChatColor.GREEN + "Game Over", (winner == null ? ChatColor.GOLD + "Nobody won! :KEKF:"
          : ChatColor.GOLD + winner.getName() + " won the game!"), 10, 70, 20);
    });

    playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);

    if (winner != null) {
      try {
        awardStat(winner, "wins");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      new BukkitRunnable() {
        int count = 5;

        @Override
        public void run() {
          if (count == 0) {
            this.cancel();
            return;
          }

          Firework fw = (Firework) getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);

          FireworkMeta fm = fw.getFireworkMeta();
          fm.setPower(2);
          fm.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).build());
          fw.setFireworkMeta(fm);
          fw.detonate();

          count--;
        }
      }.runTaskTimer(getPlugin(), 0L, 20L);
    }

    // Just make sure to call cleanup after your ending animation is done (like
    // here, its after 7 seconds)
    new BukkitRunnable() {
      @Override
      public void run() {
        cleanup();
      }
    }.runTaskLater(getPlugin(), 20 * 7);
  }

  @Override
  public void onPlayerJoin(Player player) {
    if (getStatus().equals(GameStatus.WAITING) || getStatus().equals(GameStatus.STARTING)) {
      player.teleport(getLobbyLocation());
      applyDefaults(player);
      player.setInvulnerable(true);
    } else {
      // Place in spectator
      Location loc = new Location(getWorld(), getPlugin().getConfig().getDouble("spectator.x"),
          getPlugin().getConfig().getDouble("spectator.y"), getPlugin().getConfig().getDouble("spectator.z"),
          (float) getPlugin().getConfig().getDouble("spectator.yaw"),
          (float) getPlugin().getConfig().getDouble("spectator.pitch"));

      player.teleport(loc);
      this.spectators.add(player);
      applySpectator(player);
    }
  }

  @Override
  public void onPlayerLeave(Player player) {
    applyDefaults(player);
    if (spectators.contains(player)) {
      spectators.remove(player);
    } else if (getPlayers().size() == 0 && !complete) {
      cleanup();
    }
  }

  @Override
  public void tickSecond() {
    List<SidebarString> entries = new ArrayList<>();

    if (!getStatus().equals(GameStatus.STARTED)) {
      // These lines will be displayed in the lobby. Change them however you'd like

      entries.add(new SidebarString(" "));
      entries.add(new SidebarString(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Status"));
      entries.add(new SidebarString(
          ChatColor.WHITE + ChatColor.BOLD.toString() + "  " + getStatus().toString().replaceAll("_", " ")));
      entries.add(new SidebarString("  "));
      entries.add(new SidebarString(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Players"));
      entries.add(new SidebarString(
          ChatColor.WHITE + ChatColor.BOLD.toString() + "  " + getPlayers().size() + " / " + getMaxPlayers()));
      entries.add(new SidebarString("   "));
      entries.add(new SidebarString(new String(new char[entries.size()]).replace("\0", " ")));
    } else {
      // These lines will be displayed in game. Use it to display critical game
      // information (or whatever you want)
      entries.add(new SidebarString(" "));
      entries.add(new SidebarString(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Players"));
      entries.add(new SidebarString(ChatColor.WHITE + ChatColor.BOLD.toString() + "  " + alive.size()));
      entries.add(new SidebarString("  "));
      entries.add(new SidebarString(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Spectators"));
      entries.add(new SidebarString(ChatColor.WHITE + ChatColor.BOLD.toString() + "  " + spectators.size()));
      entries.add(new SidebarString("   "));
    }

    getSidebar().setEntries(entries);

    getPlayers().forEach(x -> {
      getSidebar().showTo(x);
    });

    spectators.forEach(x -> {
      x.setFireTicks(0);
      x.setVisualFire(false);
    });
  }

  @EventHandler
  public void onDamage(EntityDamageEvent e) {
    // Check for relevance
    if (!getPlayers().contains(e.getEntity()))
      return;

    if (e.getEntity() instanceof Player) {
      Player p = (Player) e.getEntity();

      // Don't allow damage in the "celebration" phase
      if (alive.contains(p) && this.complete) {
        e.setCancelled(true);
        e.setDamage(0);
        return;
      }

      // Cancel deaths!
      if (p.getHealth() - e.getFinalDamage() < 1) {
        e.setCancelled(true);
        e.setDamage(0);

        if (!this.complete) {
          // You can move the code from here
          p.setVelocity(p.getVelocity().add(new Vector(0, 5, 0)));
          alive.remove(p);
          spectators.add(p);
          p.setFireTicks(0);
          p.setVisualFire(false);
          applySpectator(p);

          try {
            awardStat(p, "deaths");
          } catch (Exception ex) {
            ex.printStackTrace();
          }
          // to here into a kill() function if you want (then change player references as
          // needed)

          if (alive.size() == 1) {
            gameOver(alive.get(0));
          } else if (alive.size() == 0) {
            gameOver(null);
          }
        }
      }
    }
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    // Check for relevance
    if (!getPlayers().contains(e.getDamager()))
      return;

    // Don't allow spectators to hit players or entities
    if (e.getDamager() instanceof Player) {
      Player p = (Player) e.getDamager();
      if (spectators.contains(p)) {
        e.setCancelled(true);
        e.setDamage(0);
      }
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    // Check for relevance
    if (!getPlayers().contains(e.getPlayer()))
      return;

    if (spectators.contains(e.getPlayer()) || !getStatus().equals(GameStatus.STARTED)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    // Check for relevance
    if (!getPlayers().contains(e.getPlayer()))
      return;

    if (spectators.contains(e.getPlayer()) || !getStatus().equals(GameStatus.STARTED)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    // Check for relevance
    if (!getPlayers().contains(e.getPlayer()))
      return;

    if (spectators.contains(e.getPlayer()) || !getStatus().equals(GameStatus.STARTED)) {
      e.setCancelled(true);
    }
  }
}
