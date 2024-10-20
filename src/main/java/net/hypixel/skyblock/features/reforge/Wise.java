/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

public class Wise
implements Reforge {
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

