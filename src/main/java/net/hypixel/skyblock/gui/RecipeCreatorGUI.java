/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.ShapelessRecipe;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.ItemBuilder;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RecipeCreatorGUI
extends GUI {
    private static final int[] GRID = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};

    public RecipeCreatorGUI() {
        super("Admin Recipe Creator", 54);
    }

    @Override
    public void onOpen(final GUIOpenEvent e2) {
        this.fill(BLACK_STAINED_GLASS_PANE, 13, 34);
        this.border(RED_STAINED_GLASS_PANE);
        this.border(BLACK_STAINED_GLASS_PANE, 0, 44);
        this.set(24, null);
        final Player player = e2.getPlayer();
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent event) {
                player.closeInventory();
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "close").toItemStack();
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent event) {
                ItemStack result = event.getInventory().getItem(24);
                if (result == null) {
                    player.sendMessage(ChatColor.RED + "You should specify result item");
                    return;
                }
                ShapelessRecipe recipe = new ShapelessRecipe(SItem.find(result), Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result.getType()));
                recipe.getResult().setAmount(result.getAmount());
                for (ItemStack stack : RecipeCreatorGUI.this.getGridItems(e2.getInventory())) {
                    recipe.add(SItem.find(stack).getType(), stack.getAmount());
                }
                SUtil.runAsync(() -> RecipeDatabase.save(recipe));
                player.sendMessage(ChatColor.GREEN + "Successfully Saved " + recipe.getResult().getDisplayName() + " Recipe");
                User.getUser(player.getUniqueId()).getUnlockedRecipes().add(recipe.getResult().getDisplayName());
            }

            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.EMERALD).setDisplayName(ChatColor.GREEN + "Save").toItemStack();
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e2) {
        if (e2.getInventory().getItem(24) != null) {
            Sputnik.smartGiveItem(e2.getInventory().getItem(24), (Player)e2.getPlayer());
        }
        for (ItemStack stack : this.getGridItems(e2.getInventory())) {
            Sputnik.smartGiveItem(stack, (Player)e2.getPlayer());
        }
    }

    @Override
    public void onTopClick(InventoryClickEvent e2) {
        for (int slot : GRID) {
            if (slot != e2.getSlot() && e2.getSlot() != 24) continue;
            e2.setCancelled(false);
            break;
        }
    }

    public List<ItemStack> getGridItems(Inventory inventory) {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int slot : GRID) {
            if (inventory.getItem(slot) == null) continue;
            stacks.add(inventory.getItem(slot));
        }
        return stacks;
    }
}

