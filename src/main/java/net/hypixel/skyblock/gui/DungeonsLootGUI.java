/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class DungeonsLootGUI
extends GUI {
    private final Block bl;

    public DungeonsLootGUI(ItemStack loot, Block loc) {
        super("Chest", 27);
        this.bl = loc;
        this.set(13, loot, true);
    }

    @Override
    public void onClose(InventoryCloseEvent e2) {
        ItemStack[] se = e2.getInventory().getContents();
        for (int i2 = 0; i2 < se.length; ++i2) {
            if (se[i2] == null) continue;
            this.bl.getWorld().dropItemNaturally(this.bl.getLocation().clone().add(0.5, 0.8, 0.5), se[i2]);
        }
    }

    @Override
    public void onBottomClick(InventoryClickEvent e2) {
        if (e2.getAction() != InventoryAction.PLACE_ALL) {
            e2.setCancelled(true);
        } else {
            SUtil.delay(() -> e2.getWhoClicked().closeInventory(), 2L);
        }
    }

    @Override
    public void onTopClick(InventoryClickEvent e2) {
        if (e2.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            SUtil.delay(() -> e2.getWhoClicked().closeInventory(), 2L);
        }
    }
}

