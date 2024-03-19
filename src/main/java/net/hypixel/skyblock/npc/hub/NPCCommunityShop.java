package net.hypixel.skyblock.npc.hub;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class NPCCommunityShop extends SkyblockNPC {
    public NPCCommunityShop() {
        super(new NPCParameters() {
            @Override
            public String name() {
                return "community";
            }

            @Override
            public NPCType type() {
                return NPCType.VILLAGER;
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fCommunity Shop",
                        CLICK
                };
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return 0;
            }

            @Override
            public double y() {
                return 71.0;
            }

            @Override
            public double z() {
                return -101.5;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                GUIType.BOOSTER_COOKIE_SHOP.getGUI().open(player);
            }
        });
    }
}
