/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.entity.nether;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SkeletonStatistics;
import net.hypixel.skyblock.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WitherSkeleton
implements SkeletonStatistics,
EntityFunction {
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

