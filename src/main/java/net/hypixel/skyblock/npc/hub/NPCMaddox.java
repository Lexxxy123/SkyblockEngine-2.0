/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import org.bukkit.entity.Player;

public class NPCMaddox
extends SkyBlockNPC {
    public NPCMaddox() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "MADDOX_SLAYER";
            }

            @Override
            public String name() {
                return "&5Maddox the slayer";
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -75.0;
            }

            @Override
            public double y() {
                return 66.0;
            }

            @Override
            public double z() {
                return -56.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                GUIType.SLAYER.getGUI().open(player);
            }
        });
    }
}

