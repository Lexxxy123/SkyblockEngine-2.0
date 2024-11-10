/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.entity.end;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.end.BaseEnderman;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VoidlingExtremist
extends BaseEnderman {
    @Override
    public String getEntityName() {
        return ChatColor.LIGHT_PURPLE + "Voidling Extremist";
    }

    @Override
    public double getEntityMaxHealth() {
        return 8000000.0;
    }

    @Override
    public double getDamageDealt() {
        return 13500.0;
    }

    @Override
    public int mobLevel() {
        return 100;
    }

    @Override
    public double getXPDropped() {
        return 500.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(32, 64)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.ENCHANTED_ENDER_PEARL, EntityDropType.RARE, 0.05), new EntityDrop(SMaterial.SUMMONING_EYE, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
    }
}

