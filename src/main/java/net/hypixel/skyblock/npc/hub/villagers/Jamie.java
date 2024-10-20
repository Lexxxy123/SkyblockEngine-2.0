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

public class Jamie
extends SkyBlockNPC {
    public Jamie() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "JAMIE";
            }

            @Override
            public String name() {
                return "&fJamie";
            }

            @Override
            public String[] messages() {
                return new String[]{"You might have noticed that you have a Mana bar!", "Some items have mysterious properties called Abilities", "Abilities use your Mana as a resource. Here take this Rogue Sword. I don't need it!"};
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
                return -35.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -36.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
            }
        });
    }
}

