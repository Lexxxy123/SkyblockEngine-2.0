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

public class AtonedRevenant extends BaseZombie {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Atoned Revenant";
    }

    @Override
    public double getEntityMaxHealth() {
        return 2400000.0;
    }

    @Override
    public double getDamageDealt() {
        return 4800.0;
    }
    
    @Override
    public int mobLevel() {
        return 770;
    }

    @Override
    public double getXPDropped() {
        return 1600.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.IRON_SWORD)), null, SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(16777215))), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(16777215))));
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }
}
