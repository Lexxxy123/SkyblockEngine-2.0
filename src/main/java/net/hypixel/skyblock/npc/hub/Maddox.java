package net.hypixel.skyblock.npc.hub;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import org.bukkit.entity.Player;

public class Maddox extends SkyblockNPC {
    public Maddox() {
        super(new NPCParameters() {
            @Override
            public String name() {
                return "Maddox";
            }

            @Override
            public String[] holograms() {
                return new String[]{
                       "&5Maddox the slayer",
                        CLICK
                };
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -73.49;
            }

            @Override
            public double y() {
                return 65.0;
            }

            @Override
            public double z() {
                return -49.094;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                GUIType.SLAYER.getGUI().open(player);
            }
        });
    }
}
