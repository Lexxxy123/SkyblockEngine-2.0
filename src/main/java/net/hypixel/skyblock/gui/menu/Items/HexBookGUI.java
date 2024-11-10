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

import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class HexBookGUI
extends GUI {
    private SItem item;
    public boolean forceclose = false;

    public HexBookGUI(final SItem item) {
        super("The Hex -> Books", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.item = item;
        this.set(19, item.getStack());
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(SUtil.color("&d&lSUCCESS! &r&aYou applied all selected modifications to your " + item.getFullName() + "!"));
                HexBookGUI.this.forceclose = true;
                Player p = (Player)e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10.0f, 10.0f);
                new HexGUI(p.getPlayer(), item).open(p);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aApply Books And Stars"), Material.ANVIL, (short)0, 1, Sputnik.trans3("&7Knowledge is &6power&7! Apply", "&7special books to your item to", "&7upgrade it!"));
            }

            @Override
            public int getSlot() {
                return 28;
            }
        });
        MaterialStatistics statistics = item.getType().getStatistics();
        if (statistics.getType().equals((Object)GenericItemType.WEAPON) || statistics.getType().equals((Object)GenericItemType.ARMOR) || statistics.getType().equals((Object)GenericItemType.RANGED_WEAPON)) {
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e) {
                    int current = item.getHPBs();
                    if (current < 10) {
                        item.setHPBs(current + 1);
                        ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ANVIL_USE, 10.0f, 2.0f);
                        e.getWhoClicked().sendMessage(SUtil.color("&aYou added 1 Hot Potato book to your item!"));
                    } else {
                        e.getWhoClicked().sendMessage(SUtil.color("&cYou have already applied the maximum amount of Hot Potato Books to this item!"));
                        ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                    }
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(Sputnik.trans("&5Hot Potato Book"), Material.BOOK, (short)0, 1, Sputnik.trans5("&7When applied to armor, grants", "&a+2\u2748 Defense&7 and &c+4\u2764", "&cHealth&7.", "&7", "&7When applied to weapons, grants", "&c+2\u2741 Strength&7 and &c+\u27412"), Sputnik.trans4("&cDamage&7.", "&7", "&7This can be applied to an item", "&7up to &a10&7 times!"));
                }

                @Override
                public int getSlot() {
                    return 22;
                }
            });
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e) {
                    int amount = item.getStar();
                    if (item.getStar() == 5) {
                        return;
                    }
                    ItemSerial is = ItemSerial.createBlank();
                    is.saveTo(item);
                    item.setDungeonsItem(true);
                    item.setStarAmount(0);
                    item.setStarAmount(item.getStar() + amount);
                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ANVIL_USE, 10.0f, 2.0f);
                    e.getWhoClicked().sendMessage(SUtil.color("&aYou added 1 Star to your item!"));
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getSkullURLStack(ChatColor.RED + "Add Star", "8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1", 1, ChatColor.GRAY + "Add or remove stars", ChatColor.GRAY + "from your items.");
                }

                @Override
                public int getSlot() {
                    return 23;
                }
            });
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (!this.forceclose && this.item != null) {
            e.getPlayer().getInventory().addItem(new ItemStack[]{this.item.getStack()});
        }
    }
}

