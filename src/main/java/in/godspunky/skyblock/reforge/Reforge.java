package in.godspunky.skyblock.reforge;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.RarityValue;

import java.util.Arrays;
import java.util.List;

public interface Reforge {
    String getName();

    default RarityValue<Double> getStrength() {
        return RarityValue.zeroDouble();
    }

    default RarityValue<Double> getCritChance() {
        return RarityValue.zeroDouble();
    }

    default RarityValue<Double> getCritDamage() {
        return RarityValue.zeroDouble();
    }

    default RarityValue<Double> getIntelligence() {
        return RarityValue.zeroDouble();
    }

    default RarityValue<Double> getFerocity() {
        return RarityValue.zeroDouble();
    }

    default RarityValue<Double> getAttackSpeed() {
        return RarityValue.zeroDouble();
    }

    default List<GenericItemType> getCompatibleTypes() {
        return Arrays.asList(GenericItemType.values());
    }

    static Reforge blank() {
        return () -> "Blank";
    }
}
