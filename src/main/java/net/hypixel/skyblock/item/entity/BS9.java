/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.entity;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;

public class BS9
implements SkullStatistics,
MaterialFunction {
    @Override
    public String getURL() {
        return "a26ec7cd3b6ae249997137c1b94867c66e97499da071bf50adfd37034132fa03";
    }

    @Override
    public String getDisplayName() {
        return "BZB9";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }
}

