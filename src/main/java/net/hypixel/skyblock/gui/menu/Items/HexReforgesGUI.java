
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
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HexReforgesGUI extends GUI {
    private static final int[] INTERIOR = new int[]{
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
    };
    SItem upgradeableItem;
    Reforge selected;

    public HexReforgesGUI(SItem item) {
        this(item, 1);
    }

    public HexReforgesGUI(SItem item, int page) {
        super("The Hex -> Reforges", 54);
        fill(BLACK_STAINED_GLASS_PANE);
        int finalPage = page;

        PaginationList<SItem> pagedReforgeStones = new PaginationList<>(15);

        List<SItem> items = pagedReforgeStones.getPage(page);
        if (items == null) return;

        if (page > 1) {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    new HexReforgesGUI(item, finalPage - 1).open((Player) e.getWhoClicked());
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
        if (page != pagedReforgeStones.getPageCount()) {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    new HexReforgesGUI(item, finalPage + 1).open((Player) e.getWhoClicked());
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

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou selected the reforge " + SItem.find(e.getCurrentItem()).getReforge().getName() + " to be applied on your item!"));
                    selected = SItem.find(e.getCurrentItem()).getReforge();
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
                if (selected == null) {
                    e.getWhoClicked().getInventory().setItem(e.getWhoClicked().getInventory().firstEmpty(), item.getStack());
                    e.getWhoClicked().closeInventory();
                    return;
                }
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 10, 1);

                item.setReforge(selected);

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou applied the reforge " + selected.getName() + " to your " + item.getFullName() + "!"));

                new HexGUI(p.getPlayer(), item).open(p);
                upgradeableItem = null;
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack("&aApply Reforges", Material.ANVIL, (short) 0, 1,
                        Sputnik.trans3("&7Apply &aReforges&7 to your item",
                                "&7with &aReforge Stones&7 or by",
                                "&7rolling a &brandom&7 reforge.")
                );
            }
        });

    }

}