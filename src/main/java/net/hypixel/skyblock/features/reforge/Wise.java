package net.hypixel.skyblock.features.reforge;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

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
