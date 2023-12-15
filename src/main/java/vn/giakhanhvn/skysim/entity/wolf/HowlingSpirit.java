package vn.giakhanhvn.skysim.entity.wolf;

import java.util.Arrays;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

public class HowlingSpirit extends BaseWolf {
    @Override
    public String getEntityName() {
        return "Howling Spirit";
    }

    @Override
    public double getEntityMaxHealth() {
        return 7000.0;
    }

    @Override
    public double getDamageDealt() {
        return 450.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(SMaterial.SPRUCE_WOOD, EntityDropType.COMMON, 0.25), new EntityDrop(SMaterial.DARK_OAK_WOOD, EntityDropType.COMMON, 0.25), new EntityDrop(SMaterial.ACACIA_WOOD, EntityDropType.COMMON, 0.25));
    }

    @Override
    public double getXPDropped() {
        return 15.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}
