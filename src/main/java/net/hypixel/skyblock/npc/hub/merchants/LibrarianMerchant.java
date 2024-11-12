/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.LibrarianMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class LibrarianMerchant
extends SkyBlockNPC {
    public LibrarianMerchant() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "LIBRARIAN";
            }

            @Override
            public String name() {
                return "&fLibrarian";
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
                return -35.0;
            }

            @Override
            public double y() {
                return 69.0;
            }

            @Override
            public double z() {
                return -112.0;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                LibrarianMerchantGUI gui = new LibrarianMerchantGUI();
                gui.open(player);
            }
        });
    }
}

