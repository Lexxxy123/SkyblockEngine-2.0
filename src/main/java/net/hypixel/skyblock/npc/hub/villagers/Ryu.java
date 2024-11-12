/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub.villagers;

import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class Ryu
extends SkyBlockNPC {
    public Ryu() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "RYU";
            }

            @Override
            public String name() {
                return "&fRyu";
            }

            @Override
            public String[] messages() {
                return new String[]{"There are 7 Skills in Skyblock!", "Farming, Mining, Foraging, Fishing, Combat, Enchanting and Alchemy", "You can access yours skills through your Skyblock Menu"};
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
                return 25.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -116.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
            }
        });
    }
}

