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
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui.menu.Items;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class HexUltimateEnchantments
extends GUI {
    public boolean forceclose = false;
    private static final int[] INTERIOR = new int[]{12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};
    SItem upgradeableItem;
    List<EnchantmentType> selected = new ArrayList<EnchantmentType>();
    List<EnchantmentType> exists = new ArrayList<EnchantmentType>();

    public HexUltimateEnchantments(SItem item) {
        this(item, 1);
    }

    public HexUltimateEnchantments(final SItem item, int page) {
        super("The Hex -> Ultimate Enchantments", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.upgradeableItem = item;
        final int finalPage = page;
        PaginationList pagedEnchantBooks = new PaginationList(15);
        for (EnchantmentType type : EnchantmentType.ENCHANTMENT_TYPE_CACHE.values()) {
            if (!type.isUltimate() || !type.getCompatibleTypes().contains((Object)item.getType().getStatistics().getSpecificType())) continue;
            SItem bookItem = SItem.of(SMaterial.ENCHANTED_BOOK);
            bookItem.addEnchantment(type, type.maxLvl);
            pagedEnchantBooks.add(bookItem);
        }
        for (Enchantment enchantment : item.getEnchantments()) {
            this.exists.add(enchantment.getType());
        }
        final List items = pagedEnchantBooks.getPage(page);
        if (items == null) {
            return;
        }
        if (page > 1) {
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new HexUltimateEnchantments(item, finalPage - 1).open((Player)e2.getWhoClicked());
                    HexUltimateEnchantments.this.upgradeableItem = null;
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (page != pagedEnchantBooks.getPageCount()) {
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new HexUltimateEnchantments(item, finalPage + 1).open((Player)e2.getWhoClicked());
                    HexUltimateEnchantments.this.upgradeableItem = null;
                }

                @Override
                public int getSlot() {
                    return 53;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }
        int i2 = 0;
        while (i2 < items.size()) {
            final int slot = INTERIOR[i2];
            final int finalI = i2++;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    Player player = (Player)e2.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10.0f, 2.0f);
                    for (Enchantment enchantment : ((SItem)items.get(finalI)).getEnchantments()) {
                        EnchantmentType type = enchantment.getType();
                        if (HexUltimateEnchantments.this.exists.contains(type)) {
                            HexUltimateEnchantments.this.exists.remove(type);
                            item.removeEnchantment(type);
                            player.sendMessage(ChatColor.RED + "You removed the enchantment: " + type.getName() + " from your item!");
                            return;
                        }
                        if (HexUltimateEnchantments.this.selected.contains(type)) {
                            player.sendMessage(ChatColor.RED + "You removed the Enchantment " + type.getName() + " from your selection!");
                            HexUltimateEnchantments.this.selected.remove(type);
                            continue;
                        }
                        for (Enchantment enchantment1 : item.getEnchantments()) {
                            if (type.equals(EnchantmentType.ONE_FOR_ALL)) {
                                item.removeEnchantment(enchantment1.getType());
                            }
                            if (!enchantment1.getType().isUltimate()) continue;
                            item.removeEnchantment(enchantment1.getType());
                        }
                        HexUltimateEnchantments.this.selected.add(type);
                        int ultcount = 0;
                        for (EnchantmentType selectedEnchantments : HexUltimateEnchantments.this.selected) {
                            ++ultcount;
                        }
                        if (ultcount > 1) {
                            for (EnchantmentType selectedEnchantments : HexUltimateEnchantments.this.selected) {
                                HexUltimateEnchantments.this.selected.remove(selectedEnchantments);
                            }
                            player.sendMessage(ChatColor.RED + "You may not apply multiple ultimate enchantments on your item!");
                            e2.setCancelled(true);
                            return;
                        }
                        player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection but your old Ultimate Enchantment was removed!");
                        return;
                    }
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack getItem() {
                    return ((SItem)items.get(finalI)).getStack();
                }
            });
        }
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(19, item.getStack());
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                for (EnchantmentType type : HexUltimateEnchantments.this.selected) {
                    item.addEnchantment(type, type.maxLvl);
                }
                e2.getWhoClicked().sendMessage(SUtil.color("&aYou applied an Ultimate Enchantment to your " + item.getFullName()));
                HexUltimateEnchantments.this.forceclose = true;
                Player p2 = (Player)e2.getWhoClicked();
                p2.playSound(p2.getLocation(), Sound.ANVIL_USE, 10.0f, 1.0f);
                new HexGUI(p2.getPlayer(), item).open(p2);
                HexUltimateEnchantments.this.upgradeableItem = null;
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aEnchant Item"), Material.ENCHANTMENT_TABLE, (short)0, 1, Sputnik.trans("&7Add and remove enchantments from"), Sputnik.trans("&7the item in the slot above!"));
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e2) {
        if (!this.forceclose && this.upgradeableItem != null) {
            e2.getPlayer().getInventory().addItem(new ItemStack[]{this.upgradeableItem.getStack()});
        }
    }
}

