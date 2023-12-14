package vn.giakhanhvn.skysim.gui;

import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import org.bukkit.event.inventory.InventoryClickEvent;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.auction.AuctionItem;
import java.util.List;

public class YourBidsGUI extends GUI {
  private static final int[] INTERIOR = new int[] { 
      10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 
      22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 
      34, 37, 38, 39, 40, 41, 42, 43 };
  
  private List<AuctionItem> items;
  
  public YourBidsGUI() {
    super("Your Bids", 27);
    border(BLACK_STAINED_GLASS_PANE);
  }
  
  public void early(Player player) {
    User user = User.getUser(player.getUniqueId());
    this.items = user.getBids();
    this.size = Math.max(54, Double.valueOf(Math.ceil(this.items.size() / 7.0D)).intValue() * 9 + 18);
  }
  
  public void onOpen(GUIOpenEvent e) {
    final Player player = e.getPlayer();
    if (this.items == null)
      return; 
    int ended = 0;
    for (AuctionItem item : this.items) {
      if (item.isExpired())
        ended++; 
    } 
    if (ended != 0) {
      final int finalEnded = ended;
      set(new GUIClickableItem() {
            public void run(InventoryClickEvent e) {
              for (AuctionItem item : YourBidsGUI.this.items) {
                if (item.isExpired())
                  item.claim(player); 
              } 
              player.closeInventory();
            }
            
            public int getSlot() {
              return 21;
            }
            
            public ItemStack getItem() {
              List<String> lore = new ArrayList<>();
              lore.add(ChatColor.DARK_GRAY + "Ended Auctions");
              lore.add(" ");
              lore.add(ChatColor.GRAY + "You got " + ChatColor.GREEN + finalEnded + " item" + ((finalEnded != 1) ? "s" : "") + ChatColor.GRAY + " to");
              lore.add(ChatColor.GRAY + "claim items/reclaim bids.");
              lore.add(" ");
              lore.add(ChatColor.YELLOW + "Click to claim!");
              return SUtil.getStack(ChatColor.GREEN + "Claim All", Material.CAULDRON_ITEM, (short)0, 1, lore);
            }
          });
    } 
    set(GUIClickableItem.createGUIOpenerItem(GUIType.AUCTION_HOUSE, player, ChatColor.GREEN + "Go Back", 22, Material.ARROW, new String[] { ChatColor.GRAY + "To Auction House" }));
    for (int i = 0; i < this.items.size(); i++) {
      final AuctionItem item = this.items.get(i);
      final int slot = INTERIOR[i];
      set(new GUIClickableItem() {
            public void run(InventoryClickEvent e) {
              (new AuctionViewGUI(item, YourBidsGUI.this)).open(player);
            }
            
            public int getSlot() {
              return slot;
            }
            
            public ItemStack getItem() {
              return item.getDisplayItem(true, true);
            }
          });
    } 
  }
  
  private enum Sort {
    RECENTLY_UPDATED("Recently Updated"),
    HIGHEST_BID("Highest Bid"),
    MOST_BIDS("Most Bids");
    
    private final String display;
    
    Sort(String display) {
      this.display = display;
    }
    
    public String getDisplay() {
      return this.display;
    }
    
    public Sort previous() {
      int prev = ordinal() - 1;
      if (prev < 0)
        return values()[(values()).length - 1]; 
      return values()[prev];
    }
    
    public Sort next() {
      int nex = ordinal() + 1;
      if (nex > (values()).length - 1)
        return values()[0]; 
      return values()[nex];
    }
  }
}
