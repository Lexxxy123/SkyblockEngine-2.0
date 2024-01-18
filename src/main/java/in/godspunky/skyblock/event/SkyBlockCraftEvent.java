package in.godspunky.skyblock.event;


import in.godspunky.skyblock.item.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class SkyBlockCraftEvent extends SkyblockEvent {

    private final Recipe recipe;
    private final Player player;

}