/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.entity.caverns;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.EntityStatistics;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondSkeleton
implements EntityStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return "Miner Skeleton";
    }

    @Override
    public double getEntityMaxHealth() {
        return 250.0;
    }

    @Override
    public double getDamageDealt() {
        return 150.0;
    }

    @Override
    public int mobLevel() {
        return 15;
    }

    @Override
    public double getXPDropped() {
        return 20.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.BOW), new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.BONE, 4), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.MINER_HELMET, EntityDropType.RARE, 0.05), new EntityDrop(SMaterial.MINER_CHESTPLATE, EntityDropType.RARE, 0.05), new EntityDrop(SMaterial.MINER_LEGGINGS, EntityDropType.RARE, 0.05), new EntityDrop(SMaterial.MINER_BOOTS, EntityDropType.RARE, 0.05));
    }
}

