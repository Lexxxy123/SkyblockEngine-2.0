package net.hypixel.skyblock.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class SkyblockPlayerNPCClickEvent extends SkyblockEvent {

    private Player player;
    private SkyblockNPC npc;

}