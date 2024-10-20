/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import net.hypixel.skyblock.util.Sputnik;

public class EnderRelic
implements AccessoryStatistics {
    @Override
    public String getDisplayName() {
        return "Ender Relic";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(Sputnik.trans3("&7Reduces the damage taken from", "&7Endermen, Ender Dragons,", "&7and Endermites by &a25%&7."));
    }

    @Override
    public String getURL() {
        return "e02eace744e0c913778fe8b35e13e3b0549d459ae713dfc480a2b41073cd7492";
    }
}

