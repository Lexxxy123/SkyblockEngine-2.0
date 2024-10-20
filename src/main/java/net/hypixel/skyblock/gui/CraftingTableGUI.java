/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.event.SkyBlockCraftEvent;
import net.hypixel.skyblock.gui.BlockBasedGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.item.ShapelessRecipe;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftingTableGUI
extends GUI
implements BlockBasedGUI {
    private static final ItemStack RECIPE_REQUIRED = SUtil.createNamedItemStack(Material.BARRIER, ChatColor.RED + "Recipe Required");
    private static final int[] CRAFT_SLOTS;
    private static final ItemStack LOCKED_RECIPE_ITEM;
    private static final int RESULT_SLOT = 24;

    public CraftingTableGUI() {
        super("Craft Item", 54);
        this.fill(BLACK_STAINED_GLASS_PANE, 13, 34);
        this.border(RED_STAINED_GLASS_PANE);
        this.border(BLACK_STAINED_GLASS_PANE, 0, 44);
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                MaterialQuantifiable material;
                int ind;
                ItemStack stack;
                Recipe<?> recipe;
                boolean shift = e2.isShiftClick();
                Inventory inventory = e2.getClickedInventory();
                if (e2.getCurrentItem() == null || e2.getCurrentItem().getType() == Material.BARRIER || e2.getCurrentItem().getType() == Material.AIR) {
                    return;
                }
                ItemStack result = inventory.getItem(24);
                if (result == null || result.getType() == Material.AIR) {
                    return;
                }
                SItem item = SItem.find(result);
                item.setAmount(result.getAmount());
                if (!shift) {
                    if (e2.getCursor() != null && e2.getCursor().getType() != Material.AIR) {
                        SItem cursor = SItem.find(e2.getCursor());
                        if (cursor == null) {
                            cursor = SItem.convert(e2.getCursor());
                        }
                        if (!item.equals(cursor)) {
                            return;
                        }
                        if (e2.getCursor().getAmount() + result.getAmount() > 64) {
                            return;
                        }
                        e2.getCursor().setAmount(e2.getCursor().getAmount() + result.getAmount());
                    } else {
                        e2.getWhoClicked().setItemOnCursor(result);
                    }
                }
                if ((recipe = Recipe.parseRecipe(CraftingTableGUI.this.getCurrentRecipe(inventory))) == null) {
                    return;
                }
                List<MaterialQuantifiable> ingredients = recipe.getIngredients();
                ArrayList<MaterialQuantifiable> materials = new ArrayList<MaterialQuantifiable>(ingredients.size());
                for (MaterialQuantifiable materialQuantifiable : ingredients) {
                    materials.add(materialQuantifiable.clone());
                }
                int max = Integer.MAX_VALUE;
                if (shift) {
                    for (int slot : CRAFT_SLOTS) {
                        stack = inventory.getItem(slot);
                        ind = CraftingTableGUI.indexOf(recipe, materials, MaterialQuantifiable.of(stack));
                        if (ind == -1) continue;
                        material = (MaterialQuantifiable)materials.get(ind);
                        int m2 = stack.getAmount() / material.getAmount();
                        if (m2 >= max) continue;
                        max = m2;
                    }
                }
                for (int slot : CRAFT_SLOTS) {
                    stack = inventory.getItem(slot);
                    ind = CraftingTableGUI.indexOf(recipe, materials, MaterialQuantifiable.of(stack));
                    if (ind == -1) continue;
                    material = (MaterialQuantifiable)materials.get(ind);
                    int remaining = stack.getAmount() - material.getAmount();
                    if (shift) {
                        remaining = stack.getAmount() - material.getAmount() * max;
                    }
                    materials.remove(ind);
                    if (remaining <= 0) {
                        inventory.setItem(slot, new ItemStack(Material.AIR));
                        continue;
                    }
                    material.setAmount(remaining);
                    stack.setAmount(remaining);
                }
                if (shift) {
                    HashMap hashMap = e2.getWhoClicked().getInventory().addItem(new ItemStack[]{SUtil.setStackAmount(result, result.getAmount() * max)});
                    for (ItemStack stack2 : hashMap.values()) {
                        e2.getWhoClicked().getWorld().dropItem(e2.getWhoClicked().getLocation(), stack2).setVelocity(e2.getWhoClicked().getLocation().getDirection());
                    }
                }
                SkyBlockCraftEvent skyBlockCraftEvent = new SkyBlockCraftEvent(recipe, (Player)e2.getWhoClicked());
                Bukkit.getPluginManager().callEvent((Event)skyBlockCraftEvent);
                CraftingTableGUI.this.update(inventory);
            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return RECIPE_REQUIRED;
            }

            @Override
            public boolean canPickup() {
                return false;
            }
        });
        this.set(GUIClickableItem.getCloseItem(49));
    }

    @Override
    public void onOpen(final GUIOpenEvent e2) {
        new BukkitRunnable(){

            public void run() {
                final GUI gui = e2.getOpened();
                GUI current = GUI.GUI_MAP.get(e2.getPlayer().getUniqueId());
                InventoryView view = e2.getPlayer().getOpenInventory();
                if (!(current instanceof CraftingTableGUI) || view == null) {
                    this.cancel();
                    return;
                }
                final Inventory inventory = view.getTopInventory();
                new BukkitRunnable(){

                    public void run() {
                        Recipe<?> recipe = Recipe.parseRecipe(CraftingTableGUI.this.getCurrentRecipe(inventory));
                        if (recipe == null) {
                            inventory.setItem(24, RECIPE_REQUIRED);
                            SUtil.border(inventory, gui, SUtil.createColoredStainedGlassPane((short)14, ChatColor.RESET + " "), 45, 48, true, false);
                            SUtil.border(inventory, gui, SUtil.createColoredStainedGlassPane((short)14, ChatColor.RESET + " "), 50, 53, true, false);
                            return;
                        }
                        if (!recipe.isUnlockedForPlayer(User.getUser(e2.getPlayer().getUniqueId())) && !CraftingTableGUI.this.isVanilla(recipe)) {
                            inventory.setItem(24, LOCKED_RECIPE_ITEM);
                            return;
                        }
                        SItem sItem = recipe.getResult();
                        inventory.setItem(24, sItem.getStack());
                        SUtil.border(inventory, gui, SUtil.createColoredStainedGlassPane((short)5, ChatColor.RESET + " "), 45, 48, true, false);
                        SUtil.border(inventory, gui, SUtil.createColoredStainedGlassPane((short)5, ChatColor.RESET + " "), 50, 53, true, false);
                    }
                }.runTaskLaterAsynchronously((Plugin)SkyBlock.getPlugin(), 1L);
            }
        }.runTaskTimerAsynchronously((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    @Override
    public Material getBlock() {
        return Material.WORKBENCH;
    }

    private ItemStack[] getCurrentRecipe(Inventory inventory) {
        ItemStack[] stacks = new ItemStack[9];
        for (int i2 = 0; i2 < CRAFT_SLOTS.length; ++i2) {
            stacks[i2] = inventory.getItem(CRAFT_SLOTS[i2]);
        }
        return stacks;
    }

    private static int indexOf(Recipe<?> recipe, List<MaterialQuantifiable> ingredients, MaterialQuantifiable search) {
        List<SMaterial> exchangeables = Recipe.getExchangeablesOf(search.getMaterial());
        for (int i2 = 0; i2 < ingredients.size(); ++i2) {
            MaterialQuantifiable ingredient = ingredients.get(i2);
            if (recipe.isUseExchangeables() && exchangeables != null && exchangeables.contains((Object)ingredient.getMaterial()) && search.getAmount() >= ingredient.getAmount()) {
                return i2;
            }
            if (ingredient.getMaterial() != search.getMaterial() || search.getAmount() < ingredient.getAmount()) continue;
            return i2;
        }
        return -1;
    }

    private boolean isVanilla(Recipe<?> recipe) {
        if (recipe instanceof ShapedRecipe) {
            return ((ShapedRecipe)recipe).isVanilla();
        }
        if (recipe instanceof ShapelessRecipe) {
            return ((ShapelessRecipe)recipe).isVanilla();
        }
        return false;
    }

    static {
        LOCKED_RECIPE_ITEM = SUtil.createNamedItemStack(Material.BARRIER, ChatColor.RED + "Locked Recipe");
        CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    }
}

