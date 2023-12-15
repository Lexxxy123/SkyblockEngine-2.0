package vn.giakhanhvn.skysim.reforge;

import java.util.Collections;

import vn.giakhanhvn.skysim.item.GenericItemType;

import java.util.List;

import vn.giakhanhvn.skysim.item.RarityValue;

public class Hasty implements Reforge {
    @Override
    public String getName() {
        return "Hasty";
    }

    @Override
    public RarityValue<Double> getStrength() {
        return new RarityValue<Double>(3.0, 5.0, 7.0, 10.0, 15.0, 20.0);
    }

    @Override
    public RarityValue<Double> getCritChance() {
        return new RarityValue<Double>(0.2, 0.25, 0.3, 0.4, 0.5, 0.75);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.RANGED_WEAPON);
    }
}
