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

import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.reforge.ReforgeType;
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

public class HexReforgesGUI
extends GUI {
    SItem upgradeableItem;
    Reforge selected;
    public boolean forceclose = false;
    private static final int[] INTERIOR = new int[]{12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};

    public HexReforgesGUI(SItem item) {
        this(item, 1);
    }

    public HexReforgesGUI(final SItem item, int page) {
        super("The Hex -> Reforges", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        final int finalPage = page;
        PaginationList pagedReforgeStones = new PaginationList(15);
        for (ReforgeType type : ReforgeType.values()) {
            if (!type.getReforge().getCompatibleTypes().contains((Object)item.getType().getStatistics().getType()) || !type.isAccessible() || !type.getReforge().getSpecificTypes().contains((Object)item.getType().getStatistics().getSpecificType())) continue;
            SItem stone = SItem.of(SMaterial.REFORGE_STONE);
            stone.setReforge(type.getReforge());
            stone.setDisplayName("Reforge");
            pagedReforgeStones.add(stone);
        }
        final List items = pagedReforgeStones.getPage(page);
        if (items == null) {
            return;
        }
        if (page > 1) {
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new HexReforgesGUI(item, finalPage - 1).open((Player)e2.getWhoClicked());
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
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new HexReforgesGUI(item, finalPage + 1).open((Player)e2.getWhoClicked());
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
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&aYou selected the reforge " + SItem.find(e2.getCurrentItem()).getReforge().getName() + " to be applied on your item!")));
                    HexReforgesGUI.this.selected = SItem.find(e2.getCurrentItem()).getReforge();
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
                if (HexReforgesGUI.this.selected == null) {
                    e2.getWhoClicked().getInventory().setItem(e2.getWhoClicked().getInventory().firstEmpty(), item.getStack());
                    e2.getWhoClicked().closeInventory();
                    return;
                }
                Player p2 = (Player)e2.getWhoClicked();
                p2.playSound(p2.getLocation(), Sound.ANVIL_USE, 10.0f, 1.0f);
                HexReforgesGUI.this.forceclose = true;
                item.setReforge(HexReforgesGUI.this.selected);
                p2.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&aYou applied the reforge " + HexReforgesGUI.this.selected.getName() + " to your " + item.getFullName() + "!")));
                new HexGUI(p2.getPlayer(), item).open(p2);
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aApply Reforges"), Material.ANVIL, (short)0, 1, Sputnik.trans3("&7Apply &aReforges&7 to your item", "&7with &aReforge Stones&7 or by", "&7rolling a &brandom&7 reforge."));
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

