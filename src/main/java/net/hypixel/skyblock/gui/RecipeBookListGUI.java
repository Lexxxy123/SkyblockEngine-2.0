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
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.gui.RecipeBookGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RecipeBookListGUI
extends GUI {
    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    public RecipeBookListGUI(String query, int page, Player player) {
        super("Recipe Book", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        if (player == null) {
            return;
        }
        PaginationList pagedMaterials = new PaginationList(28);
        for (ShapedRecipe sr : ShapedRecipe.CACHED_RECIPES) {
            String lc = sr.getResult().getType().toString().toLowerCase();
            if (!sr.isUnlockedForPlayer(User.getUser(player.getUniqueId())) || sr.isVanilla()) continue;
            pagedMaterials.add(sr.getResult());
        }
        if (pagedMaterials.isEmpty()) {
            page = 0;
        }
        this.title = "Recipe Book (" + page + "/" + pagedMaterials.getPageCount() + ")";
        final int finalPage = page;
        if (finalPage > 1) {
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e) {
                    new RecipeBookListGUI(finalPage - 1).open((Player)e.getWhoClicked());
                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
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
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e) {
                    new RecipeBookListGUI(finalPage + 1).open((Player)e.getWhoClicked());
                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
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
        this.set(GUIClickableItem.getCloseItem(49));
        List p = pagedMaterials.getPage(page);
        if (p == null) {
            return;
        }
        for (int i = 0; i < p.size(); ++i) {
            final int slot = INTERIOR[i];
            final SItem sItem = (SItem)p.get(i);
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e) {
                    Player player = (Player)e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
                    new RecipeBookGUI(sItem).open(player);
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

    @Override
    public void onOpen(GUIOpenEvent e) {
        Player player = e.getPlayer();
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkyBlock Menu"));
    }

    public RecipeBookListGUI(String query, Player player) {
        this(query, 1, player);
    }

    public RecipeBookListGUI(int page) {
        this("", page, null);
    }

    public RecipeBookListGUI() {
        this("", 1, null);
    }

    public RecipeBookListGUI(Player player) {
        this("", 1, player);
    }
}

