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
import net.hypixel.skyblock.user.User;
import org.bukkit.entity.Player;

public class Stella
extends SkyBlockNPC {
    public Stella() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "STELLA";
            }

            @Override
            public String name() {
                return "&fStella";
            }

            @Override
            public String[] messages() {
                return new String[]{"At any time you can create a Co-op with your friends", "Simply go to your Skyblock Menu where you can find the Profile Menu", "Enter /coop followed by the name of all the friends you want to invite!"};
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
                return 19.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -99.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                if (!User.getUser(player).getTalkedVillagers().contains(this.name())) {
                    User.getUser(player).getTalkedVillagers().add(this.name());
                }
            }
        });
    }
}

