/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.LumberMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import org.bukkit.entity.Player;

public class LumberMerchant
extends SkyBlockNPC {
    public LumberMerchant() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "LUMBER_JACK";
            }

            @Override
            public String name() {
                return "&fLumberJack";
            }

            @Override
            public String[] messages() {
                return new String[]{"Buy and sell wood and axes with me!", "Click me again to open the Lumberjack Shop!"};
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -49.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -68.0;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                LumberMerchantGUI gui = new LumberMerchantGUI();
                gui.open(player);
            }
        });
    }
}

