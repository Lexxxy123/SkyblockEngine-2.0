package in.godspunky.skyblock.event;

import in.godspunky.skyblock.features.npc.NPC;
import in.godspunky.skyblock.npc.SkyblockNPC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class SkyblockPlayerNPCClickEvent extends SkyblockEvent {

    private Player player;
    private NPC npc;

}
