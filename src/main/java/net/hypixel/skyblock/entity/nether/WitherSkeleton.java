package net.hypixel.skyblock.entity.nether;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SkeletonStatistics;
import net.hypixel.skyblock.item.SMaterial;

import java.util.Arrays;
import java.util.List;

public class WitherSkeleton implements SkeletonStatistics, EntityFunction {
    @Override
    public String getEntityName() {
        return "Wither Skeleton";
    }

    @Override
    public double getEntityMaxHealth() {
        return 250.0;
    }

    @Override
    public double getDamageDealt() {
        return 152.0;
    }
    
    @Override
    public int mobLevel() {
        return 10;
    }

    @Override
    public boolean isWither() {
        return true;
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.BONE, 3), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.COAL, EntityDropType.COMMON, 0.5), new EntityDrop(SMaterial.ENCHANTED_COAL, EntityDropType.OCCASIONAL, 0.025), new EntityDrop(SMaterial.ENCHANTED_CHARCOAL, EntityDropType.RARE, 0.01));
    }

    @Override
    public double getXPDropped() {
        return 13.0;
    }
}
