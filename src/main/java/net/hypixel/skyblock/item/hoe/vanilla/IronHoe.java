/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.hoe.vanilla;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;

public class IronHoe
implements ToolStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Iron Hoe";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.TOOL;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HOE;
    }
}

