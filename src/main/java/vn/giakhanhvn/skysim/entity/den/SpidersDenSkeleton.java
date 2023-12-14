package vn.giakhanhvn.skysim.entity.den;

import java.util.Collections;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.entity.EntityStatistics;

public class SpidersDenSkeleton implements EntityStatistics, EntityFunction
{
    @Override
    public String getEntityName() {
        return "Skeleton";
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 100.0;
    }
    
    @Override
    public double getDamageDealt() {
        return 33.0;
    }
    
    @Override
    public double getXPDropped() {
        return 6.0;
    }
    
    @Override
    public List<EntityDrop> drops() {
        return Collections.<EntityDrop>singletonList(new EntityDrop(new ItemStack(Material.BONE, SUtil.random(5, 7)), EntityDropType.GUARANTEED, 1.0));
    }
}
