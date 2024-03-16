package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.user.User;
import org.bukkit.event.EventHandler;

public class TalkToMort extends Objective {
    public TalkToMort() {
        super("talk_to_mort", "Talk To Mort");
    }

    @EventHandler
    public void onClick(SkyblockPlayerNPCClickEvent e) {
        if (!isThisObjective(e.getPlayer())) return;

        if (User.getUser(e.getPlayer()).getTalkedNPCs().contains("Mort")) complete(e.getPlayer());
    }

}
