package vn.giakhanhvn.skysim.gui;

import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.auction.AuctionItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.auction.AuctionEscrow;
import vn.giakhanhvn.skysim.user.User;

public class AuctionConfirmGUI extends GUI {
    public AuctionConfirmGUI() {
        super("Confirm", 27);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(e.getPlayer().getUniqueId());
        final AuctionEscrow escrow = user.getAuctionEscrow();
        if (escrow == null) {
            return;
        }
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                final AuctionItem item = AuctionItem.createAuction(escrow.getItem(), escrow.getStarter(), System.currentTimeMillis() + escrow.getDuration(), user.getUuid(), user.isAuctionCreationBIN());
                user.subCoins(escrow.getCreationFee(user.isAuctionCreationBIN()));
                user.setAuctionEscrow(new AuctionEscrow());
                new AuctionViewGUI(item).open(player);
            }

            @Override
            public ItemStack getItem() {
                final int count = escrow.getItem().getStack().getAmount();
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short) 13, 1, ChatColor.GRAY + "Auctioning: " + ((count != 1) ? (count + "x ") : "") + escrow.getItem().getFullName(), ChatColor.GRAY + "Cost: " + ChatColor.GOLD + escrow.getCreationFee(user.isAuctionCreationBIN()) + " coins");
            }

            @Override
            public int getSlot() {
                return 11;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                e.getWhoClicked().closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short) 14, 1);
            }

            @Override
            public int getSlot() {
                return 15;
            }
        });
    }
}
