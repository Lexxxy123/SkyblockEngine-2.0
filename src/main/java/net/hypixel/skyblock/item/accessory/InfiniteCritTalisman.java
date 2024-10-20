/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;

public class InfiniteCritTalisman
implements AccessoryStatistics,
MaterialFunction {
    @Override
    public String getURL() {
        return "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c";
    }

    @Override
    public String getDisplayName() {
        return "Infinite Crit Talisman";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }

    @Override
    public double getBaseCritChance() {
        return 1.0;
    }
}

