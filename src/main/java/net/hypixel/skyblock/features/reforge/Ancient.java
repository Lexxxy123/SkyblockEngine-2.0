/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

public class Ancient
implements Reforge {
    @Override
    public String getName() {
        return "Ancient";
    }

    @Override
    public RarityValue<Double> getStrength() {
        return new RarityValue<Double>(4.0, 8.0, 12.0, 18.0, 25.0, 35.0);
    }

    @Override
    public RarityValue<Double> getCritChance() {
        return new RarityValue<Double>(0.03, 0.05, 0.07, 0.09, 0.12, 0.15);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.ARMOR);
    }
}

