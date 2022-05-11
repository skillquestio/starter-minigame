package io.skillquest.mc.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.skillquest.mc.ServerPlugin;

/**
 * PagedInventory is an extension to the ClickableInventory class which provides
 * a way to easily page out a list of items. This class will take care of
 * pagination so that it accurately displays the items that it should.
 */
public class PagedInventory extends ClickableInventory implements Listener {

  private int bIndex, fIndex, sIndex, eIndex, cIndex, page;
  private Material bMat, fMat;
  private List<ItemStack> items;

  public PagedInventory(int cIndex, int sIndex, int eIndex, int bIndex, int fIndex, int rows, String title) {
    this(cIndex, sIndex, eIndex, bIndex, fIndex, Material.YELLOW_WOOL, Material.YELLOW_WOOL, rows, title);
  }

  public PagedInventory(int cIndex, int sIndex, int eIndex, int bIndex, int fIndex, Material bMat, Material fMat,
      int rows, String title) {
    super(rows, title);

    this.bMat = bMat;
    this.fMat = fMat;
    this.sIndex = sIndex;
    this.eIndex = eIndex;
    this.bIndex = bIndex;
    this.fIndex = fIndex;
    this.cIndex = cIndex;
    this.page = 1;
    this.items = new ArrayList<>();

    ServerPlugin.pm.registerEvents(this, ServerPlugin.pl);
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPage() {
    return this.page;
  }

  public void addItem(ItemStack is) {
    this.items.add(is);
    this.refresh();
  }

  public List<ItemStack> getItems() {
    return this.items;
  }

  public void setItems(List<ItemStack> items) {
    this.items = items;

    this.refresh();
  }

  /**
   * Refreshes the current paged inventory. If you have refreshable items you want
   * to display alongside the paged content, override the refresh() method in your
   * initializion and dont forget to call super.refresh() on the first line of the
   * function (prior to your inventory updates) so that the paged inventory works
   * correctly!
   */
  @Override
  public void refresh() {
    this.clicks.clear();

    // Total items / Display size
    int pages = (int) Math.ceil((((double) items.size()) / (double) ((eIndex - sIndex + 1
        - (bIndex >= sIndex && bIndex <= eIndex ? 1 : 0) - (fIndex >= sIndex && fIndex <= eIndex ? 1 : 0)))));

    // Display items from list
    for (int i = sIndex; i <= eIndex; i++) {
      if (i != bIndex && i != fIndex) {
        // Set item (or null)
        int itemsPerPage = eIndex - sIndex + 1;
        int curIndex = (i - sIndex) + (itemsPerPage * (page - 1));
        if (curIndex >= items.size()) {
          setItem(i, null);
        } else {
          setItem(i, items.get(curIndex));
        }
      }
    }

    // Show current page info
    ItemStack cur = new ItemStack(Material.PAPER, 1);
    ItemMeta im = cur.getItemMeta();
    im.setDisplayName(ChatColor.AQUA + "Page " + page + " / " + pages);
    cur.setItemMeta(im);
    setItem(cIndex, cur);

    ItemStack hidden = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    im = hidden.getItemMeta();
    im.setDisplayName(" ");
    hidden.setItemMeta(im);

    // Show/hide previous page button
    if (page > 1) {
      ItemStack bi = new ItemStack(bMat, 1);
      im = bi.getItemMeta();
      im.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "< Previous Page");
      bi.setItemMeta(im);
      setItemWithClick(bIndex, bi, new Click(bi) {
        @Override
        public void run(InventoryClickEvent event) {
          setPage(page - 1);
          refresh();
        }
      });
    } else {
      setItem(bIndex, hidden);
    }

    // Show/hiden next page button
    if (page < pages) {
      ItemStack fi = new ItemStack(fMat, 1);
      im = fi.getItemMeta();
      im.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Next Page >");
      fi.setItemMeta(im);
      setItemWithClick(fIndex, fi, new Click(fi) {
        @Override
        public void run(InventoryClickEvent event) {
          setPage(page + 1);
          refresh();
        }
      });
    } else {
      setItem(fIndex, hidden);
    }
  }
}
