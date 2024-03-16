package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class BossRoom extends Objective {
    public BossRoom() {
        super("boss_room","Complete F6 Boss Room");
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        if (!isThisObjective(e.getPlayer())) return;

        if(e.getPlayer().getWorld().getName().contains("f6")) complete(e.getPlayer());
    }
}
