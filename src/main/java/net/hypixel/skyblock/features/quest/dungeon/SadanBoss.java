package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.event.SkyblockEntityDeathEvent;
import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.event.EventHandler;

public class SadanBoss extends Objective {
    public SadanBoss() {
        super("sadan_boss", "Fight Sadan Boss");
    }

    @EventHandler
    public void onKillEvent(SkyblockEntityDeathEvent e){
        if (e.getKiller() == null) return;
        if (!isThisObjective(e.getKiller().toBukkitPlayer())) return;
        if (e.getEntity().getSpecType() == SEntityType.SADAN) {
            long zombies =  e.getKiller().sadancollections;
            e.getKiller().sadancollections = zombies + 1;

            if (zombies == 25) complete(e.getKiller().toBukkitPlayer());
        }
    }
}
