/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SlayerCancellationConfirmGUI
extends GUI {
    public SlayerCancellationConfirmGUI(final User user) {
        super("Confirm", 27);
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                user.setSlayerQuest(null);
                e2.getWhoClicked().sendMessage(ChatColor.GREEN + "Your Slayer Quest has been cancelled!");
                GUIType.SLAYER.getGUI().open((Player)e2.getWhoClicked());
                user.removeAllSlayerBosses();
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short)13, 1, ChatColor.RED + "Clears " + ChatColor.GRAY + "progress towards", ChatColor.GRAY + "the current Slayer Quest to", ChatColor.GRAY + "let you pick a different", ChatColor.GRAY + "one.", "", ChatColor.RED + "" + ChatColor.BOLD + "CANCELLING THE QUEST!", ChatColor.YELLOW + "Click to cancel the quest!");
            }

            @Override
            public int getSlot() {
                return 11;
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                e2.getWhoClicked().closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short)14, 1, new String[0]);
            }

            @Override
            public int getSlot() {
                return 15;
            }
        });
    }
}

