/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.RarityValue;

public class HeroicReforge
implements Reforge {
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

