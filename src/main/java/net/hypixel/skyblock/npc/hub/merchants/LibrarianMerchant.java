package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.LibrarianMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class LibrarianMerchant extends SkyblockNPC {
    public LibrarianMerchant(NPCParameters npcParameters) {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "Librarian";
            }

            @Override
            public String[] messages() {
                return new String[]{
                        ""
                };
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fLibrarian",
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
                return -35;
            }

            @Override
            public double y() {
                return 69;
            }

            @Override
            public double z() {
                return -112;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                LibrarianMerchantGUI gui = new LibrarianMerchantGUI();
                gui.open(player);
            }
        });
    }
}
