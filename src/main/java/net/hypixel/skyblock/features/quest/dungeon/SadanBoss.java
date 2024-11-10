/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 */
package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.event.SkyblockEntityDeathEvent;
import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.event.EventHandler;

public class SadanBoss
extends Objective {
    public SadanBoss() {
        super("sadan_boss", "Fight Sadan Boss");
    }

    @EventHandler
    public void onKillEvent(SkyblockEntityDeathEvent e) {
        if (e.getKiller() == null) {
            return;
        }
        if (!this.isThisObjective(e.getKiller().toBukkitPlayer())) {
            return;
        }
        if (e.getEntity().getSpecType() == SEntityType.SADAN) {
            long zombies = e.getKiller().sadancollections;
            e.getKiller().sadancollections = zombies + 1L;
            if (zombies == 25L) {
                this.complete(e.getKiller().toBukkitPlayer());
            }
        }
    }
}

