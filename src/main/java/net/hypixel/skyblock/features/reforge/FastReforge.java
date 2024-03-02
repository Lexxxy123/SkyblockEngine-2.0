package net.hypixel.skyblock.features.reforge;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class FastReforge implements Reforge {
    @Override
    public String getName() {
        return "Fast";
    }

    @Override
    public RarityValue<Double> getAttackSpeed() {
        return new RarityValue<Double>(10.0, 20.0, 30.0, 40.0, 50.0, 60.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
