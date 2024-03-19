package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.LibrarianMerchantGUI;
import net.hypixel.skyblock.features.merchant.MineMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class MineMerchant extends SkyblockNPC {
    public MineMerchant() {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "Miner";
            }

            @Override
            public String[] messages() {
                return new String[]{
                        "My specialities are ores, stone, and mining equipment.",
                        "Click me again to open the Miner Shop!"
                };
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fMiner",
                        "&e&lCLICK",
                };
            }

            @Override
            public NPCType type() {
                return NPCType.PLAYER;
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -8;
            }

            @Override
            public double y() {
                return 68;
            }

            @Override
            public double z() {
                return -124;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                MineMerchantGUI gui = new MineMerchantGUI();
                gui.open(player);
            }
        });
    }
}
