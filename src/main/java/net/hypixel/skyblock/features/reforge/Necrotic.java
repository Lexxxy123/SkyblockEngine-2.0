/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

public class Necrotic
implements Reforge {
    @Override
    public String getName() {
        return "Necrotic";
    }

    @Override
    public RarityValue<Double> getIntelligence() {
        return new RarityValue<Double>(30.0, 60.0, 90.0, 120.0, 150.0, 200.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes() {
        return Collections.singletonList(GenericItemType.ARMOR);
    }
}

