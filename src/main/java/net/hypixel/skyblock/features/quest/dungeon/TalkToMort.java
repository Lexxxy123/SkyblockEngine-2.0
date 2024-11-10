/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 */
package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.event.EventHandler;

public class TalkToMort
extends Objective {
    public TalkToMort() {
        super("talk_to_mort", "Talk To Mort");
    }

    @EventHandler
    public void onClick(SkyblockPlayerNPCClickEvent event) {
        if (!this.isThisObjective(event.getPlayer())) {
            return;
        }
        if (event.getNpc().getName().equalsIgnoreCase("Mort")) {
            this.complete(event.getPlayer());
        }
    }
}

