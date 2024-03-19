package net.hypixel.skyblock.npc.hub;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.NPCSkin;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class NPCJerry extends SkyblockNPC {
    public NPCJerry() {
        super(new NPCParameters() {
            @Override
            public String name() {
                return "jerry";
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fSandbox Jerry",
                        CLICK
                };
            }

            @Override
            public String[] messages() {
                return new String[]{
                        "Welcome to Godspunky Skyblock Sandbox!",
                        "The Skyblock universe is full of islands to explore and resources to discover!",
                        "I will help you start your journey in sandbox.",
                        "Use /ib or free items npc to get starter goods.",
                        "Use /enc <enchant type> <level> to enchant!",
                        "Complete Flor 6 Boss Room.",
                        "Complete All Slayer Bosses.",
                        "New Updates Coming Soon in future!"

                };
            }

            @Override
            public NPCType type() {
                return NPCType.VILLAGER;
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -2;
            }

            @Override
            public double y() {
                return 70;
            }

            @Override
            public double z() {
                return -79;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {

            }
        });
    }
}
