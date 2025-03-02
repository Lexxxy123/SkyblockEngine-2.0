/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.storage;

import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.storage.Storage;

public class JumboBackpack
extends Storage
implements SkullStatistics {
    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public String getDisplayName() {
        return "Jumbo Backpack";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public String getURL() {
        return "1f8405116c1daa7ce2f012591458d50246d0a467bcb95a5a2c033aefd6008b63";
    }
}

