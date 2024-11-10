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

import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class HexModifiersGUI
extends GUI {
    SItem upgradeableItem;
    public boolean forceclose = false;

    public HexModifiersGUI(final SItem item) {
        super("The Hex -> Modifiers", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.upgradeableItem = item;
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                HexModifiersGUI.this.upgradeableItem = item;
                item.setRecombobulated(true);
                Player player = (Player)e.getWhoClicked();
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10.0f, 2.0f);
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&aYou recombobulated your " + item.getFullName() + "!")));
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SItem.of(SMaterial.RECOMBOBULATOR_3000).getStack();
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&d&lSUCCESS! &r&dYou modified your " + item.getFullName() + "!")));
                HexModifiersGUI.this.forceclose = true;
                Player p = (Player)e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 10.0f, 1.0f);
                new HexGUI(p.getPlayer(), item).open(p);
                HexModifiersGUI.this.upgradeableItem = null;
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aApply Modifiers"), Material.ANVIL, (short)0, 1, Sputnik.trans5("&7Apply miscellaneous item", "&7modifiers like the", "&6Recombobulator 3000&7,", "&5Wither Scrolls&7, and &cMaster", "&cStars&7!"));
            }
        });
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(19, item.getStack());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (!this.forceclose && this.upgradeableItem != null) {
            e.getPlayer().getInventory().addItem(new ItemStack[]{this.upgradeableItem.getStack()});
        }
    }
}

