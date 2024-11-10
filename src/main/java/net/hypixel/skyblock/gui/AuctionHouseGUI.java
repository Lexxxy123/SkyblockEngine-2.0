/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.gui.AuctionsBrowserGUI;
import net.hypixel.skyblock.gui.CreateAuctionGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.ManageAuctionsGUI;
import net.hypixel.skyblock.gui.YourBidsGUI;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AuctionHouseGUI
extends GUI {
    public AuctionHouseGUI() {
        super("Auction House", 36);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.set(GUIClickableItem.getCloseItem(31));
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "Auctions Browser", Material.GOLD_BLOCK, (short)0, 1, ChatColor.GRAY + "Find items for sale by", ChatColor.GRAY + "players across SkySim!", " ", ChatColor.GRAY + "Items offered here are for", ChatColor.GOLD + "auction" + ChatColor.GRAY + ", meaning you", ChatColor.GRAY + "have to place the top bid", ChatColor.GRAY + "to acquire them!", " ", ChatColor.YELLOW + "Click to browse!");
            }
        });
        final List<AuctionItem> bids = user.getBids();
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (bids.size() == 0) {
                    return;
                }
                new YourBidsGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                ArrayList<String> lore = new ArrayList<String>();
                long held = bids.stream().filter(item -> item.getTopBidder().getUuid().equals(user.getUuid())).count();
                ChatColor color = (long)bids.size() == held ? ChatColor.GREEN : ChatColor.RED;
                ChatColor chatColor = color;
                if (bids.size() == 0) {
                    lore.add(ChatColor.GRAY + "You don't have any");
                    lore.add(ChatColor.GRAY + "outstanding bids.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Use the " + ChatColor.GOLD + "Auctions Browser");
                    lore.add(ChatColor.GRAY + "to find some cool items.");
                } else if (bids.size() == 1 && ((AuctionItem)bids.get(0)).getTopBidder().getUuid().equals(user.getUuid())) {
                    lore.add(ChatColor.GRAY + "You have the " + ChatColor.GREEN + "top bid" + ChatColor.GRAY + " on");
                    lore.add(ChatColor.GRAY + "a pending auction.");
                } else {
                    lore.add(ChatColor.GRAY + "You placed " + ChatColor.GREEN + bids.size() + " bid" + (bids.size() != 1 ? "s" : "") + ChatColor.GRAY + " on");
                    lore.add(ChatColor.GRAY + "pending auctions.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "You hold the top bid on");
                    lore.add(ChatColor.GRAY + "(" + color + held + ChatColor.GRAY + "/" + color + bids.size() + ChatColor.GRAY + ") of these");
                    lore.add(ChatColor.GRAY + "auctions.");
                }
                for (AuctionItem item2 : bids) {
                    if (!item2.getTopBidder().getUuid().equals(user.getUuid()) || !item2.isExpired()) continue;
                    lore.add(" ");
                    lore.add(ChatColor.AQUA + "* You have items/coins to pickup!");
                    break;
                }
                if (bids.size() > 0) {
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to view!");
                }
                return SUtil.getStack((bids.size() == 0 ? ChatColor.YELLOW : ChatColor.GREEN) + "View Bids", Material.GOLDEN_CARROT, (short)0, 1, lore);
            }
        });
        final List<AuctionItem> auctions = user.getAuctions();
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (auctions.size() == 0) {
                    new CreateAuctionGUI().open(player);
                } else {
                    new ManageAuctionsGUI().open(player);
                }
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                ArrayList<String> lore = new ArrayList<String>();
                if (auctions.size() == 0) {
                    lore.add(ChatColor.GRAY + "Set your own items on");
                    lore.add(ChatColor.GRAY + "auction for other players");
                    lore.add(ChatColor.GRAY + "to purchase.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to become rich!");
                } else {
                    lore.add(ChatColor.GRAY + "You own " + ChatColor.YELLOW + auctions.size() + " auction" + (auctions.size() != 1 ? "s" : "") + ChatColor.GRAY + " in");
                    lore.add(ChatColor.GRAY + "progress or which recently");
                    lore.add(ChatColor.GRAY + "ended.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Players can find your auctions");
                    lore.add(ChatColor.GRAY + "using the " + ChatColor.GOLD + "Auctions Browser");
                    lore.add(ChatColor.GRAY + "or typing " + ChatColor.GREEN + "/ah " + player.getName() + ChatColor.GRAY + "!");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to manage!");
                }
                return SUtil.getStack(ChatColor.GREEN + (auctions.size() == 0 ? "Create Auction" : "Manage Auctions"), Material.GOLD_BARDING, (short)0, 1, lore);
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 32;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Auction Stats", Material.EMPTY_MAP, (short)0, 1, ChatColor.GRAY + "View various statistics", ChatColor.GRAY + "about you and the Auction", ChatColor.GRAY + "House.", " ", ChatColor.RED + "Coming soon!");
            }
        });
    }
}

