/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.inventory.CraftItemEvent
 */
package net.hypixel.skyblock.features.quest.starting;

import net.hypixel.skyblock.event.SkyBlockCraftEvent;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

public class WorkbenchObjective
extends Objective {
    public WorkbenchObjective() {
        super("craft_workbench", "Craft a workbench");
    }

    @EventHandler
    public void onCraft(SkyBlockCraftEvent e2) {
        if (!this.isThisObjective(e2.getPlayer())) {
            return;
        }
        if (e2.getRecipe().getResult().getType().equals((Object)SMaterial.CRAFTING_TABLE)) {
            this.complete(e2.getPlayer());
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e2) {
        if (!this.isThisObjective((Player)e2.getWhoClicked())) {
            return;
        }
        if (e2.getRecipe().getResult().getType().equals((Object)Material.WORKBENCH)) {
            this.complete(((Player)e2.getWhoClicked()).getPlayer());
        }
    }
}

