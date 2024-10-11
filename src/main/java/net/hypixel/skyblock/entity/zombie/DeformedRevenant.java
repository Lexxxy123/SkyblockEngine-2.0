package net.hypixel.skyblock.entity.zombie;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;

import java.util.Collections;
import java.util.List;

public class DeformedRevenant extends BaseZombie {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Deformed Revenant";
    }

    @Override
    public double getEntityMaxHealth() {
        return 360000.0;
    }

    @Override
    public double getDamageDealt() {
        return 4400.0;
    }
    
    @Override
    public int mobLevel() {
        return 300;
    }

    @Override
    public double getXPDropped() {
        return 1200.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.GOLD_SWORD)), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(15217459))), SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(15217459))), new ItemStack(Material.IRON_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }
}
