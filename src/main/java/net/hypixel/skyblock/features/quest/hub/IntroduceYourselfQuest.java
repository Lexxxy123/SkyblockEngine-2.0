/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 */
package net.hypixel.skyblock.features.quest.hub;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class IntroduceYourselfQuest
extends QuestLine {
    private static List<String> VILLAGERS = Arrays.asList("TOM", "STELLA", "DUKE", "LIAM", "JACK", "JAMIE", "RYU", "LYNN", "LEO", "VEX", "FELIX", "ANDREW");

    public IntroduceYourselfQuest() {
        super("introduce_yourself", "Introduce Yourself", new TalkToVillagersObjective());
    }

    @Override
    protected List<String> getRewards() {
        return Collections.singletonList(ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + 50 + " &7Coins");
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    @Override
    protected void reward(User player) {
        player.addCoins(50L);
    }

    private static class TalkToVillagersObjective
    extends Objective {
        public TalkToVillagersObjective() {
            super("talk_to_villagers", "Explore Hub");
        }

        @Override
        public String getSuffix(User player) {
            return ChatColor.GRAY + "(" + ChatColor.YELLOW + player.getTalkedVillagers().size() + ChatColor.GRAY + "/" + ChatColor.GREEN + "12" + ChatColor.GRAY + ")";
        }

        @EventHandler
        public void onClick(SkyblockPlayerNPCClickEvent e2) {
            if (!this.isThisObjective(e2.getPlayer())) {
                return;
            }
            SkyBlockNPC npc = e2.getNpc();
            if (VILLAGERS.contains(npc.getId())) {
                User.getUser(e2.getPlayer()).addTalkedVillager(npc.getId());
            }
            if (User.getUser(e2.getPlayer()).getTalkedVillagers().size() >= 12) {
                this.complete(e2.getPlayer());
            }
        }
    }
}

