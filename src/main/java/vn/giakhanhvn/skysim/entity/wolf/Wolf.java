package vn.giakhanhvn.skysim.entity.wolf;

import java.util.Collections;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

public class Wolf extends BaseWolf {
    @Override
    public String getEntityName() {
        return "Wolf";
    }

    @Override
    public double getEntityMaxHealth() {
        return 250.0;
    }

    @Override
    public double getDamageDealt() {
        return 80.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.BONE, EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getXPDropped() {
        return 10.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }

    @Override
    public int mobLevel() {
        return 15;
    }
}
