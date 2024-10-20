/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.Reforgable;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.SpecificItemType;

public interface AccessoryStatistics
extends PlayerBoostStatistics,
SkullStatistics,
Reforgable {
    @Override
    default public GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    @Override
    default public SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }
}

