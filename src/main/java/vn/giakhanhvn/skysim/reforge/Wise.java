package vn.giakhanhvn.skysim.reforge;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class Wise implements Reforge {
    @Override
    public String getName() {
        return "Wise";
    }

    @Override
    public RarityValue<Double> getIntelligence() {
        return new RarityValue<Double>(25.0, 50.0, 75.0, 100.0, 125.0, 150.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.ARMOR);
    }
}
