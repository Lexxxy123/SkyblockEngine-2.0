package in.godspunky.skyblock.features.reforge;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class HeroicReforge implements Reforge {
    @Override
    public String getName() {
        return "Heroic";
    }

    @Override
    public RarityValue<Double> getStrength() {
        return new RarityValue<Double>(15.0, 20.0, 25.0, 32.0, 40.0, 50.0);
    }

    @Override
    public RarityValue<Double> getIntelligence() {
        return new RarityValue<Double>(40.0, 50.0, 65.0, 80.0, 100.0, 125.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
