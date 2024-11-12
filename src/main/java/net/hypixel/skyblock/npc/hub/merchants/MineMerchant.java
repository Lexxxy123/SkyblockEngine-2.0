/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.MineMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import org.bukkit.entity.Player;

public class MineMerchant
extends SkyBlockNPC {
    public MineMerchant() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "MINER_MERCHANT";
            }

            @Override
            public String name() {
                return "&fMine";
            }

            @Override
            public String[] messages() {
                return new String[]{"My specialities are ores, stone, and mining equipment.", "Click me again to open the Miner Shop!"};
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -8.0;
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
                MineMerchantGUI gui = new MineMerchantGUI();
                gui.open(player);
            }
        });
    }
}

