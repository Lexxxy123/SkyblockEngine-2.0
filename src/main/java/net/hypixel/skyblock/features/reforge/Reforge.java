/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;
import net.hypixel.skyblock.item.SpecificItemType;

public interface Reforge {
    public String getName();

    default public RarityValue<Double> getStrength() {
        return RarityValue.zeroDouble();
    }

    default public RarityValue<Double> getCritChance() {
        return RarityValue.zeroDouble();
    }

    default public RarityValue<Double> getCritDamage() {
        return RarityValue.zeroDouble();
    }

    default public RarityValue<Double> getIntelligence() {
        return RarityValue.zeroDouble();
    }

    default public RarityValue<Double> getFerocity() {
        return RarityValue.zeroDouble();
    }

    default public RarityValue<Double> getAttackSpeed() {
        return RarityValue.zeroDouble();
    }

    default public List<GenericItemType> getCompatibleTypes() {
        return Arrays.asList(GenericItemType.values());
    }

    public static Reforge blank() {
        return () -> "Blank";
    }

    default public List<SpecificItemType> getSpecificTypes() {
        return Arrays.asList(SpecificItemType.values());
    }
}

