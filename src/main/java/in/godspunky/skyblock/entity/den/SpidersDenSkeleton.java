package in.godspunky.skyblock.entity.den;

import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.EntityStatistics;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
