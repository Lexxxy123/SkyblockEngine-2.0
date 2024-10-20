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

public class Lynn
extends SkyBlockNPC {
    public Lynn() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "LYNN";
            }

            @Override
            public String name() {
                return "&fLynn";
            }

            @Override
            public String[] messages() {
                return new String[]{"If you ever get lost during a quest, open your Quest Log in your SkyBlock Menu!"};
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
                return -21.0;
            }

            @Override
            public double y() {
                return 68.0;
            }

            @Override
            public double z() {
                return -124.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
            }
        });
    }
}

