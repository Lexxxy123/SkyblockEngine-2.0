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
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class NPCBlackSmith
extends SkyBlockNPC {
    public NPCBlackSmith() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "BLACKSMITH";
            }

            @Override
            public String name() {
                return "&fBlacksmith";
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
                return -28.5;
            }

            @Override
            public double y() {
                return 69.0;
            }

            @Override
            public double z() {
                return -125.5;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                GUIType.REFORGE_ANVIL.getGUI().open(player);
            }
        });
    }
}

