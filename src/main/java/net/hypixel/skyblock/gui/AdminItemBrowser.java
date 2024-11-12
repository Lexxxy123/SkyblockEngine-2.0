/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.List;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIQueryItem;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminItemBrowser
extends GUI {
    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    public AdminItemBrowser(String query, int page) {
        super("Admin Item Browser", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        PaginationList pagedMaterials = new PaginationList(28);
        for (SMaterial mat : SMaterial.values()) {
            if (!mat.name().toLowerCase().contains("dwarven_") && !mat.name().toLowerCase().contains("hidden_") && !mat.name().toLowerCase().contains("bzb") && !mat.name().toLowerCase().contains("god_pot")) continue;
            pagedMaterials.add(mat);
        }
        if (!query.isEmpty()) {
            pagedMaterials.removeIf(material -> !material.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase()));
        }
        if (pagedMaterials.isEmpty()) {
            page = 0;
        }
        this.title = "Admin Item Browser (" + page + "/" + pagedMaterials.getPageCount() + ")";
        if (page > 1) {
            final int finalPage = page;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new AdminItemBrowser(finalPage - 1).open((Player)e2.getWhoClicked());
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Pervious Page");
                }
            });
        }
        if (page != pagedMaterials.getPageCount()) {
            final int finalPage1 = page;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new AdminItemBrowser(finalPage1 + 1).open((Player)e2.getWhoClicked());
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
                }

                @Override
                public int getSlot() {
                    return 53;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Next Page");
                }
            });
        }
        this.set(new GUIQueryItem(){

            @Override
            public GUI onQueryFinish(String query) {
                return new AdminItemBrowser(query);
            }

            @Override
            public void run(InventoryClickEvent e2) {
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createNamedItemStack(Material.SIGN, ChatColor.GREEN + "Search");
            }
        });
        this.set(GUIClickableItem.getCloseItem(50));
        List p2 = pagedMaterials.getPage(page);
        if (p2 != null) {
            for (int i2 = 0; i2 < p2.size(); ++i2) {
                final int slot = INTERIOR[i2];
                final SItem sItem = SItem.of((SMaterial)((Object)p2.get(i2)), ((SMaterial)((Object)p2.get(i2))).getData());
                this.set(new GUIClickableItem(){

                    @Override
                    public void run(InventoryClickEvent e2) {
                        Player player = (Player)e2.getWhoClicked();
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
                        player.sendMessage(ChatColor.GOLD + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GREEN + " has been added to your inventory.");
                        player.getInventory().addItem(new ItemStack[]{SItem.of(sItem.getType(), sItem.getVariant()).getStack()});
                    }

                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack getItem() {
                        return sItem.getStack();
                    }
                });
            }
        }
    }

    public AdminItemBrowser(String query) {
        this(query, 1);
    }

    public AdminItemBrowser(int page) {
        this("", page);
    }

    public AdminItemBrowser() {
        this(1);
    }
}

