package vn.giakhanhvn.skysim.entity.caverns;

import java.util.Collections;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import vn.giakhanhvn.skysim.entity.EntityStatistics;
import vn.giakhanhvn.skysim.entity.EntityFunction;

public class Pigman implements EntityFunction, EntityStatistics {
    @Override
    public String getEntityName() {
        return "Pigman";
    }

    @Override
    public double getEntityMaxHealth() {
        return 250.0;
    }

    @Override
    public double getDamageDealt() {
        return 75.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SItem.of(SMaterial.GOLD_SWORD).getStack(), null, null, null, null);
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.GOLD_NUGGET).getStack(), SUtil.random(1, 2)), EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getXPDropped() {
        return 20.0;
    }
}
