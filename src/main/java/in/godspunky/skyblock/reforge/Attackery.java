package in.godspunky.skyblock.reforge;

import in.godspunky.skyblock.item.RarityValue;

public class Attackery implements Reforge {
    @Override
    public String getName() {
        return "Attackery";
    }

    @Override
    public RarityValue<Double> getAttackSpeed() {
        return RarityValue.singleDouble(20.0);
    }
}
