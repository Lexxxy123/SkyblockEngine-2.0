
package net.hypixel.skyblock.gui.menu.Items;


import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIItem;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
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

import java.util.ArrayList;
import java.util.List;

public class HexUltimateEnchantments extends GUI {

    public boolean forceclose = false;
    private static final int[] INTERIOR = new int[]{
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
    };
    SItem upgradeableItem;
    List<EnchantmentType> selected = new ArrayList<>();
    List<EnchantmentType> exists = new ArrayList<>();

    public HexUltimateEnchantments(SItem item) {
        this(item, 1);
    }

    public HexUltimateEnchantments(SItem item, int page) {
        super("The Hex -> Ultimate Enchantments", 54);
        fill(BLACK_STAINED_GLASS_PANE);
        upgradeableItem = item;
        int finalPage = page;

        PaginationList<SItem> pagedEnchantBooks = new PaginationList<>(15);

        for (EnchantmentType type : EnchantmentType.ENCHANTMENT_TYPE_CACHE.values()) {
            if (!type.isUltimate()) continue;
            if (!(type.getCompatibleTypes().contains(item.getType().getStatistics().getSpecificType()))) continue;

            SItem bookItem = SItem.of(SMaterial.ENCHANTED_BOOK);

            bookItem.addEnchantment(type, type.maxLvl);

            pagedEnchantBooks.add(bookItem);
        }

        for (Enchantment enchantment : item.getEnchantments()) {
            exists.add(enchantment.getType());
        }

        List<SItem> items = pagedEnchantBooks.getPage(page);
        if (items == null) return;

        if (page > 1) {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    new HexUltimateEnchantments(item, finalPage - 1).open((Player) e.getWhoClicked());
                    upgradeableItem = null;
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
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    new HexUltimateEnchantments(item, finalPage + 1).open((Player) e.getWhoClicked());
                    upgradeableItem = null;
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

        for (int i = 0; i < items.size(); i++) {
            int slot = INTERIOR[i];
            int finalI = i;
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    Player player = (Player) e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 2);

                    for (Enchantment enchantment : items.get(finalI).getEnchantments()) {
                        EnchantmentType type = enchantment.getType();

                        if (exists.contains(type)) {
                            exists.remove(type);
                            item.removeEnchantment(type);
                            player.sendMessage(ChatColor.RED + "You removed the enchantment: " + type.getName() + " from your item!");
                            return;
                        } else {
                            if (selected.contains(type)) {
                                player.sendMessage(ChatColor.RED + "You removed the Enchantment " + type.getName() + " from your selection!");
                                selected.remove(type);
                            } else {
                                for (Enchantment enchantment1 : item.getEnchantments()) {
                                    if (type.equals(EnchantmentType.ONE_FOR_ALL)) {
                                        item.removeEnchantment(enchantment1.getType());
                                    }

                                    if (enchantment1.getType().isUltimate())
                                        item.removeEnchantment(enchantment1.getType());
                                }

                                selected.add(type);

                                int ultcount = 0;
                                for (EnchantmentType selectedEnchantments : selected) {
                                    ultcount++;
                                }

                                if (ultcount > 1) {
                                    for (EnchantmentType selectedEnchantments : selected) {
                                        selected.remove(selectedEnchantments);
                                    }
                                    player.sendMessage(ChatColor.RED + "You may not apply multiple ultimate enchantments on your item!");
                                    e.setCancelled(true);
                                    return;
                                }

                                player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection but your old Ultimate Enchantment was removed!");
                                return;
                            }
                        }
                    }
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack getItem() {
                    return items.get(finalI).getStack();
                }
            });
        }


        set(GUIClickableItem.getCloseItem(49));
        set(19, item.getStack());

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                for (EnchantmentType type : selected) {
                    item.addEnchantment(type, type.maxLvl);
                }

                e.getWhoClicked().sendMessage(SUtil.color("&aYou applied an Ultimate Enchantment to your " + item.getFullName()));
                forceclose = true;
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 10, 1);

                new HexGUI(p.getPlayer(), item).open(p);
                upgradeableItem = null;
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack("&aEnchant Item", Material.ENCHANTMENT_TABLE, (short) 0, 1,
                        Sputnik.trans("&7Add and remove enchantments from"),
                        Sputnik.trans("&7the item in the slot above!")
                );
            }
        });

    }
    @Override
    public void onClose(InventoryCloseEvent e){
        if(!forceclose) {
            if (upgradeableItem != null) {
                e.getPlayer().getInventory().addItem(upgradeableItem.getStack());
            }
        }
    }

}