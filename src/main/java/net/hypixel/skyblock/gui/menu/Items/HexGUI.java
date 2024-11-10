/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.gui.menu.Items;

import java.io.IOException;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.menu.Items.HexBookGUI;
import net.hypixel.skyblock.gui.menu.Items.HexEnchantments;
import net.hypixel.skyblock.gui.menu.Items.HexModifiersGUI;
import net.hypixel.skyblock.gui.menu.Items.HexReforgesGUI;
import net.hypixel.skyblock.gui.menu.Items.HexUltimateEnchantments;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HexGUI
extends GUI {
    public boolean forceclose = false;
    private Inventory inventory;
    SItem upgradeableItem;

    public HexGUI(final Player player, SItem item) {
        super("The Hex", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.upgradeableItem = item;
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (HexGUI.this.upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                } else {
                    HexGUI.this.forceclose = true;
                    new HexReforgesGUI(HexGUI.this.upgradeableItem).open(player);
                    HexGUI.this.upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 33;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aReforges"), "94af5f3b50e2dea2687d292cc3a5e42c028b8626e638842fb4f78671debe276c", 1, Sputnik.trans3("&7Apply &aReforges&7 to your item", "&7with &aReforge Stones&7 or by", "&7rolling a &brandom&7 reforge."));
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (HexGUI.this.upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                } else {
                    HexGUI.this.forceclose = true;
                    new HexModifiersGUI(HexGUI.this.upgradeableItem).open(player);
                    HexGUI.this.upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 25;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aModifiers"), "57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1, Sputnik.trans5("&7Apply miscellaneous item", "&7modifiers like the", "&6Recombobulator 3000&7,", "&5Wither Scrolls&7, and &cMaster", "&cStars&7!"));
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (HexGUI.this.upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                } else {
                    HexGUI.this.forceclose = true;
                    new HexEnchantments(HexGUI.this.upgradeableItem).open(player);
                    HexGUI.this.upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aEnchantments"), Material.ENCHANTMENT_TABLE, (short)0, 1, Sputnik.trans5("&7This special &aEnchantment Table", "&7allows you to access way more", "&7Enchantments and gives you the", "&7option to consume &aBottles of", "&aEnchanting&7 directly!"));
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (HexGUI.this.upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                } else {
                    HexGUI.this.forceclose = true;
                    new HexUltimateEnchantments(HexGUI.this.upgradeableItem).open(player);
                    HexGUI.this.upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 16;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aUltimate Enchantments"), Material.BOOK_AND_QUILL, (short)0, 1, Sputnik.trans4("&7Allows you to apply &d&lUltimate", "&d&lEnchantments&r&7 and gives you the", "&7option to consume &aBottles of", "&aEnchanting&7 directly!"));
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                if (HexGUI.this.upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                } else {
                    HexGUI.this.forceclose = true;
                    new HexBookGUI(HexGUI.this.upgradeableItem).open(player);
                    HexGUI.this.upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aBooks"), Material.BOOK, (short)0, 1, Sputnik.trans3("&7Knowledge is &6power!&7 Apply", "&7special books to your item to", "&7upgrade it!"));
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent p0) {
            }

            @Override
            public ItemStack getItem() {
                if (HexGUI.this.upgradeableItem != null) {
                    return HexGUI.this.upgradeableItem.getStack();
                }
                return new ItemStack(Material.AIR);
            }

            @Override
            public int getSlot() {
                return 22;
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 12;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 14;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 30;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return 32;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short)10, " ");
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (!this.forceclose && this.upgradeableItem != null) {
            e.getPlayer().getInventory().addItem(new ItemStack[]{this.upgradeableItem.getStack()});
        }
    }

    @Override
    public void onOpen(GUIOpenEvent e) throws IOException {
        this.inventory = e.getInventory();
    }

    @Override
    public void onBottomClick(InventoryClickEvent e) {
        ItemStack selected = e.getCurrentItem();
        if (selected == null || selected.getType() == Material.AIR) {
            return;
        }
        SItem item = SItem.find(selected);
        if (item == null) {
            item = SItem.convert(selected);
        }
        PlayerInventory playerInventory = e.getWhoClicked().getInventory();
        this.upgradeableItem = item;
        playerInventory.remove(selected);
        this.inventory.setItem(22, this.upgradeableItem.getStack());
    }

    public HexGUI(Player player) {
        this(player, null);
    }
}

