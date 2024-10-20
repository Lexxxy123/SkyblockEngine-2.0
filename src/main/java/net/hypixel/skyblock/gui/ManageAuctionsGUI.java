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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.gui.AuctionViewGUI;
import net.hypixel.skyblock.gui.CreateAuctionGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ManageAuctionsGUI
extends GUI {
    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16};
    private final Sort sort;

    public ManageAuctionsGUI(Sort sort) {
        super("Manage Auctions", 27);
        this.sort = sort;
        this.border(BLACK_STAINED_GLASS_PANE);
    }

    public ManageAuctionsGUI() {
        this(Sort.RECENTLY_UPDATED);
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        final Player player = e2.getPlayer();
        User user = User.getUser(player.getUniqueId());
        Stream stream = user.getAuctions().stream();
        switch (this.sort) {
            case RECENTLY_UPDATED: {
                stream = stream.sorted((i1, i2) -> {
                    AuctionBid b1 = i1.getRecentBid();
                    AuctionBid b2 = i2.getRecentBid();
                    if (b1 == null) {
                        return 1;
                    }
                    if (b2 == null) {
                        return -1;
                    }
                    long t1 = b1.getTimestamp();
                    long t2 = b2.getTimestamp();
                    return Long.compare(t2, t1);
                });
                break;
            }
            case HIGHEST_BID: {
                stream = stream.sorted((i1, i2) -> {
                    AuctionBid b3 = i1.getTopBid();
                    AuctionBid b4 = i2.getTopBid();
                    if (b3 == null) {
                        return 1;
                    }
                    if (b4 == null) {
                        return -1;
                    }
                    return Long.compare(b3.getAmount(), b4.getAmount());
                });
                break;
            }
            case MOST_BIDS: {
                stream = stream.sorted((i1, i2) -> Integer.compare(i2.getBids().size(), i1.getBids().size()));
            }
        }
        final List items = stream.collect(Collectors.toList());
        int ended = 0;
        for (final AuctionItem item : items) {
            if (!item.isExpired()) continue;
            ++ended;
        }
        if (ended != 0) {
            final int finalEnded = ended;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    for (AuctionItem item : items) {
                        if (!item.isExpired()) continue;
                        item.claim(player);
                    }
                    player.closeInventory();
                }

                @Override
                public int getSlot() {
                    return 21;
                }

                @Override
                public ItemStack getItem() {
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.DARK_GRAY + "Ended Auctions");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "You got " + ChatColor.GREEN + finalEnded + " item" + (finalEnded != 1 ? "s" : "") + ChatColor.GRAY + " to");
                    lore.add(ChatColor.GRAY + "collect sales/reclaim items.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to claim!");
                    return SUtil.getStack(ChatColor.GREEN + "Claim All", Material.CAULDRON_ITEM, (short)0, 1, lore);
                }
            });
        }
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.AUCTION_HOUSE, player, ChatColor.GREEN + "Go Back", 22, Material.ARROW, ChatColor.GRAY + "To Auction House"));
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                if (e2.isRightClick()) {
                    new ManageAuctionsGUI(ManageAuctionsGUI.this.sort.previous()).open(player);
                } else {
                    new ManageAuctionsGUI(ManageAuctionsGUI.this.sort.next()).open(player);
                }
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                ArrayList<String> lore = new ArrayList<String>(Collections.singletonList(" "));
                for (Sort s2 : Sort.values()) {
                    if (ManageAuctionsGUI.this.sort == s2) {
                        lore.add(ChatColor.AQUA + "\u25b6 " + s2.getDisplay());
                        continue;
                    }
                    lore.add(ChatColor.GRAY + s2.getDisplay());
                }
                lore.add(" ");
                lore.add(ChatColor.AQUA + "Right-Click to go backwards!");
                lore.add(ChatColor.YELLOW + "Click to switch sort!");
                return SUtil.getStack(ChatColor.GREEN + "Sort", Material.HOPPER, (short)0, 1, lore);
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                if (items.size() >= 7) {
                    player.sendMessage(ChatColor.RED + "You cannot create any more auctions!");
                    return;
                }
                new CreateAuctionGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Create Auction", Material.GOLD_BARDING, (short)0, 1, ChatColor.GRAY + "Set your own items on", ChatColor.GRAY + "auction for other players", ChatColor.GRAY + "to purchase.", " ", ChatColor.YELLOW + "Click to become rich!");
            }
        });
        for (int i3 = 0; i3 < items.size(); ++i3) {
            AuctionItem item;
            item = (AuctionItem)items.get(i3);
            final int slot = INTERIOR[i3];
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new AuctionViewGUI(item, ManageAuctionsGUI.this).open(player);
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack getItem() {
                    return item.getDisplayItem(true, true);
                }
            });
        }
    }

    private static enum Sort {
        RECENTLY_UPDATED("Recently Updated"),
        HIGHEST_BID("Highest Bid"),
        MOST_BIDS("Most Bids");

        private final String display;

        private Sort(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return this.display;
        }

        public Sort previous() {
            int prev = this.ordinal() - 1;
            if (prev < 0) {
                return Sort.values()[Sort.values().length - 1];
            }
            return Sort.values()[prev];
        }

        public Sort next() {
            int nex = this.ordinal() + 1;
            if (nex > Sort.values().length - 1) {
                return Sort.values()[0];
            }
            return Sort.values()[nex];
        }
    }
}

