package Starter.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

/**
 * Mathematical helper functions used in the plugin.
 */
public class MathFunctions {
  public static List<Location> getSphere(Location centerBlock, int radius, boolean hollow) {
    if (centerBlock == null) {
      return new ArrayList<>();
    }

    List<Location> circleBlocks = new ArrayList<Location>();

    int bx = centerBlock.getBlockX();
    int by = centerBlock.getBlockY();
    int bz = centerBlock.getBlockZ();

    for (int x = bx - radius; x <= bx + radius; x++) {
      for (int y = by - radius; y <= by + radius; y++) {
        for (int z = bz - radius; z <= bz + radius; z++) {

          double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

          if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

            Location l = new Location(centerBlock.getWorld(), x, y, z);

            circleBlocks.add(l);

          }

        }
      }
    }

    return circleBlocks;
  }
}
