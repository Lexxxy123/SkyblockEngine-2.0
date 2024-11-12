/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.BlockBreakEvent
 */
package net.hypixel.skyblock.features.quest.starting;

import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakLogObjective
extends Objective {
    public BreakLogObjective() {
        super("break_log", "Break a log");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e2) {
        if (!this.isThisObjective(e2.getPlayer())) {
            return;
        }
        if (e2.getBlock().getType().equals((Object)Material.LOG)) {
            this.complete(e2.getPlayer());
        }
    }
}

