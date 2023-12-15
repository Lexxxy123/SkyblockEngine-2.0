package vn.giakhanhvn.skysim.entity.zombie;

import java.util.Collections;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;

public class CryptGhoul extends BaseZombie {
    @Override
    public String getEntityName() {
        return "Crypt Ghoul";
    }

    @Override
    public double getEntityMaxHealth() {
        return 2000.0;
    }

    @Override
    public double getDamageDealt() {
        return 350.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.IRON_SWORD), null, new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS));
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(new ItemStack(Material.ROTTEN_FLESH, SUtil.random(1, 3)), EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getMovementSpeed() {
        return 0.35;
    }

    @Override
    public double getXPDropped() {
        return 30.0;
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
        return 30;
    }
}
