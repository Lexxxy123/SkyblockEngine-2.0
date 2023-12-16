package vn.giakhanhvn.skysim.entity.den;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.entity.EntityStatistics;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.Collections;
import java.util.List;

public class SpidersDenSkeleton implements EntityStatistics, EntityFunction {
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
        return Collections.singletonList(new EntityDrop(new ItemStack(Material.BONE, SUtil.random(5, 7)), EntityDropType.GUARANTEED, 1.0));
    }
}
