/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

public class WitheredReforge
implements Reforge {
    @Override
    public String getName() {
        return "Withered";
    }

    @Override
    public RarityValue<Double> getStrength() {
        return new RarityValue<Double>(60.0, 75.0, 90.0, 110.0, 135.0, 170.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}

