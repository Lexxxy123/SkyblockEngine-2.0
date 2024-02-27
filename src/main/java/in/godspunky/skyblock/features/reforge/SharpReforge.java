package in.godspunky.skyblock.features.reforge;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class SharpReforge implements Reforge {
    @Override
    public String getName() {
        return "Sharp";
    }

    @Override
    public RarityValue<Double> getCritChance() {
        return new RarityValue<Double>(0.1, 0.12, 0.14, 0.17, 0.2, 0.25);
    }

    @Override
    public RarityValue<Double> getCritDamage() {
        return new RarityValue<Double>(0.2, 0.3, 0.4, 0.55, 0.75, 0.9);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
