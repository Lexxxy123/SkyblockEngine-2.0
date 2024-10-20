/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.RarityValue;

public class Attackery
implements Reforge {
    @Override
    public String getName() {
        return "Attackery";
    }

    @Override
    public RarityValue<Double> getAttackSpeed() {
        return RarityValue.singleDouble(20.0);
    }
}

