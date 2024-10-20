/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import net.hypixel.skyblock.util.Sputnik;

public class GoldenJerryArtifact
implements AccessoryStatistics {
    @Override
    public String getDisplayName() {
        return "Golden Jerry Artifact";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public double getBaseIntelligence() {
        return -4.0;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(Sputnik.trans3("&7Wear to prove you love Jerry,", "&7or wear to prove you hate", "&7Jerry."));
    }

    @Override
    public String getURL() {
        return "f27075e621002d12e4d550b91d680bc2ebaced859199f93feb3df18178a7594b";
    }

    @Override
    public double getBaseStrength() {
        return 450.0;
    }
}

