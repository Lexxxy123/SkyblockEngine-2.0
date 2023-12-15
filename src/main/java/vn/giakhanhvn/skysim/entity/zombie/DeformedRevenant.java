package vn.giakhanhvn.skysim.entity.zombie;

import java.util.Collections;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

import org.bukkit.Color;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import net.md_5.bungee.api.ChatColor;

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
