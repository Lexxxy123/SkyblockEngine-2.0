/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.entity.zombie;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldenGhoul
extends BaseZombie {
    @Override
    public String getEntityName() {
        return ChatColor.GOLD + "Golden Ghoul";
    }

    @Override
    public double getEntityMaxHealth() {
        return 45000.0;
    }

    @Override
    public double getDamageDealt() {
        return 800.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.GOLD_SWORD), null, new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ROTTEN_FLESH, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(new ItemStack(Material.GOLD_INGOT, SUtil.random(1, 11)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.GOLDEN_POWDER, EntityDropType.CRAZY_RARE, 0.006));
    }

    @Override
    public double getXPDropped() {
        return 50.0;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean isVillager() {
        return false;
    }

    @Override
    public int mobLevel() {
        return 60;
    }
}

