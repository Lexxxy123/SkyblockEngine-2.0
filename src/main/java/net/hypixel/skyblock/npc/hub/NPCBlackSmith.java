package net.hypixel.skyblock.npc.hub;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class NPCBlackSmith extends SkyblockNPC {

    public NPCBlackSmith() {
        super(new NPCParameters() {
            @Override
            public String name() {
                return "blacksmith";
            }

            @Override
            public NPCType type() {
                return NPCType.VILLAGER;
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fBlacksmith",
                        CLICK
                };
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
            public void onInteract(Player player, SkyblockNPC npc) {
                GUIType.REFORGE_ANVIL.getGUI().open(player);
            }
        });
    }
}
