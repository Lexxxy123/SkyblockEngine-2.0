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

public class Leo
extends SkyBlockNPC {
    public Leo() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "LEO";
            }

            @Override
            public String name() {
                return "&fLeo";
            }

            @Override
            public String[] messages() {
                return new String[]{"You can unlock Leaflet Armor by progressing through your Oak Log Collection", "There is a Forest west of the Village where you can gather Oak Logs", "To check your Collection progress and rewards, open the Collection Menu in your Skyblock Menu"};
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
                return -7.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -75.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
            }
        });
    }
}

