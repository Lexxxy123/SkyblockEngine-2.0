package vn.giakhanhvn.skysim.entity.zombie;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.Collections;
import java.util.List;

public class RevenantChampion extends BaseZombie {
    @Override
    public String getEntityName() {
        return "Revenant Champion";
    }

    @Override
    public double getEntityMaxHealth() {
        return 90000.0;
    }

    @Override
    public double getDamageDealt() {
        return 2200.0;
    }

    @Override
    public double getXPDropped() {
        return 600.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), null, SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(new ItemStack(Material.CHAINMAIL_LEGGINGS)), new ItemStack(Material.IRON_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), 2), EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public boolean isBaby() {
        return false;
    }
}
