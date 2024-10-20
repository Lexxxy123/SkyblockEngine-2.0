/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item;

import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemCategory;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;

public interface MaterialStatistics {
    public String getDisplayName();

    public Rarity getRarity();

    default public String getLore() {
        return null;
    }

    default public List<String> getListLore() {
        return null;
    }

    public GenericItemType getType();

    default public SpecificItemType getSpecificType() {
        return SpecificItemType.NONE;
    }

    default public boolean isStackable() {
        return true;
    }

    default public boolean isEnchanted() {
        return false;
    }

    default public boolean displayKills() {
        return false;
    }

    default public boolean displayCoins() {
        return false;
    }

    default public List<String> killReplacementLore() {
        return null;
    }

    default public boolean displayRarity() {
        return true;
    }

    default public ItemCategory getCategory() {
        return ItemCategory.TOOLS_MISC;
    }

    default public long getPrice() {
        return 1L;
    }

    default public long getValue() {
        return 1L;
    }

    default public void load() {
    }
}

