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
import java.util.Objects;
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

public class HexEnchantments
extends GUI {
    private static final int[] INTERIOR = new int[]{12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};
    public boolean forceclose = false;
    SItem upgradeableItem;
    List<EnchantmentType> selected = new ArrayList<EnchantmentType>();
    List<EnchantmentType> exists = new ArrayList<EnchantmentType>();

    public HexEnchantments(SItem item) {
        this(item, 1);
    }

    public HexEnchantments(final SItem item, final int page) {
        super("The Hex -> Enchantments", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.upgradeableItem = item;
        PaginationList pagedEnchantBooks = new PaginationList(15);
        for (EnchantmentType type : EnchantmentType.ENCHANTMENT_TYPE_CACHE.values()) {
            if (type.isUltimate() || !type.getCompatibleTypes().contains((Object)Objects.requireNonNull(item.getType().getStatistics()).getSpecificType())) continue;
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
                    HexEnchantments.this.forceclose = true;
                    new HexEnchantments(item, page - 1).open((Player)e2.getWhoClicked());
                    HexEnchantments.this.upgradeableItem = null;
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
                    HexEnchantments.this.forceclose = true;
                    new HexEnchantments(item, page + 1).open((Player)e2.getWhoClicked());
                    HexEnchantments.this.upgradeableItem = null;
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
                    HexEnchantments.this.forceclose = true;
                    Player player = (Player)e2.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10.0f, 2.0f);
                    for (Enchantment enchantment : ((SItem)items.get(finalI)).getEnchantments()) {
                        EnchantmentType type = enchantment.getType();
                        if (HexEnchantments.this.exists.contains(type)) {
                            HexEnchantments.this.exists.remove(type);
                            item.removeEnchantment(type);
                            player.sendMessage(ChatColor.RED + "You removed the enchantment: " + type.getName() + " from your item!");
                            return;
                        }
                        if (HexEnchantments.this.selected.contains(type)) {
                            player.sendMessage(ChatColor.RED + "You removed the Enchantment " + type.getName() + " from your selection!");
                            HexEnchantments.this.selected.remove(type);
                            return;
                        }
                        if (HexEnchantments.this.selected.contains(type)) continue;
                        if (item.hasEnchantment(EnchantmentType.ONE_FOR_ALL)) {
                            player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection but One For All was removed!");
                            HexEnchantments.this.selected.add(type);
                            item.removeEnchantment(EnchantmentType.ONE_FOR_ALL);
                        } else {
                            player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection!");
                            HexEnchantments.this.selected.add(type);
                        }
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
                for (EnchantmentType type : HexEnchantments.this.selected) {
                    item.addEnchantment(type, type.maxLvl);
                }
                HexEnchantments.this.forceclose = true;
                e2.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&d&lSUCCESS! &r&dYou enchanted your " + item.getFullName() + "!")));
                Player p2 = (Player)e2.getWhoClicked();
                p2.playSound(p2.getLocation(), Sound.ANVIL_USE, 10.0f, 1.0f);
                new HexGUI(p2.getPlayer(), item).open(p2);
                HexEnchantments.this.upgradeableItem = null;
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

