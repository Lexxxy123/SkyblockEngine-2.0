package vn.giakhanhvn.skysim.item.exclusive;

import vn.giakhanhvn.skysim.entity.SEntityType;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.SkullStatistics;

public abstract class FloatingCrystal implements SkullStatistics, MaterialFunction
{
    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }
    
    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }
    
    @Override
    public void onInteraction(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        final Player player = e.getPlayer();
        final SEntity sEntity = new SEntity(player.getLocation().clone().add(player.getLocation().getDirection().multiply(1.5)), this.getCrystalType(), new Object[0]);
    }
    
    protected abstract SEntityType getCrystalType();
}
