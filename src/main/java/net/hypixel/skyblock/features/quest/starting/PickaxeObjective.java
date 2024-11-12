/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 */
package net.hypixel.skyblock.features.quest.starting;

import net.hypixel.skyblock.event.SkyBlockCraftEvent;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.item.SMaterial;
import org.bukkit.event.EventHandler;

public class PickaxeObjective
extends Objective {
    public PickaxeObjective() {
        super("craft_pickaxe", "Craft a wood pickaxe");
    }

    @EventHandler
    public void onCraft(SkyBlockCraftEvent e2) {
        if (!this.isThisObjective(e2.getPlayer())) {
            return;
        }
        if (e2.getRecipe().getResult().getType().equals((Object)SMaterial.WOOD_PICKAXE)) {
            this.complete(e2.getPlayer());
        }
    }
}

