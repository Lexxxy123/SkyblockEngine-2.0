/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.storage;

import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.storage.Storage;

public class SmallBackpack
extends Storage
implements SkullStatistics {
    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public String getDisplayName() {
        return "Small Backpack";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public String getURL() {
        return "21d837ca222cbc0bc12426f5da018c3a931b406008800960a9df112a596e7d62";
    }
}

