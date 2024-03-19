package net.hypixel.skyblock.npc.hub.merchants;



import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public class AdventurerMerchant extends SkyblockNPC {
    public AdventurerMerchant() {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "Adventurer";
            }

            @Override
            public String[] messages() {
                return new String[]{
                        "I've seen it all - every island from here to the edge of the world!",
                        "Over the years I've acquired a variety of Talismans and Artifacts.",
                        "For a price, you can have it all!",
                        "Click me again to open the Adventurer Shop!"
                };
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fAdventurer",
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
                return -41.0;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -64.0;
            }

            @Override
            public float yaw() {
                return 97.8751f;
            }

            @Override
            public float pitch() {
                return 0.0f;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {

            }
        });
    }
}