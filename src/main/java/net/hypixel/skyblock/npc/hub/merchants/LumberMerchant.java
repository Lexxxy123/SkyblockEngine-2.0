package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.LibrarianMerchantGUI;
import net.hypixel.skyblock.features.merchant.LumberMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class LumberMerchant extends SkyblockNPC {
    public LumberMerchant() {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "LumberJack";
            }

            @Override
            public String[] messages() {
                return new String[]{
                        "Buy and sell wood and axes with me!",
                        "Click me again to open the Lumberjack Shop!"
                };
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fLumberJack",
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
                return -49;
            }

            @Override
            public double y() {
                return 70;
            }

            @Override
            public double z() {
                return -68;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                LumberMerchantGUI gui = new LumberMerchantGUI();
                gui.open(player);
            }
        });
    }
}
