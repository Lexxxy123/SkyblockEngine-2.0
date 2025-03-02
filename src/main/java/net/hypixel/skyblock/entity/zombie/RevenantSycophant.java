/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.entity.zombie;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RevenantSycophant
extends BaseZombie {
    @Override
    public String getEntityName() {
        return "Revenant Sycophant";
    }

    @Override
    public double getEntityMaxHealth() {
        return 24000.0;
    }

    @Override
    public double getDamageDealt() {
        return 850.0;
    }

    @Override
    public double getXPDropped() {
        return 300.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), null, SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(new ItemStack(Material.CHAINMAIL_LEGGINGS)), new ItemStack(Material.IRON_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.REVENANT_FLESH, EntityDropType.GUARANTEED, 1.0));
    }
}

