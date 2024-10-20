/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;

public class RedClawArtifact
implements AccessoryStatistics,
MaterialFunction {
    private static final Map<UUID, Integer> HITS = new HashMap<UUID, Integer>();

    @Override
    public String getURL() {
        return "caf59b8aa0f83546ef0d178ccf87e7ed88cf7858caae79b3633cbd75b650525f";
    }

    @Override
    public String getDisplayName() {
        return "Red Claw Artifact";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public double getBaseCritChance() {
        return 0.05;
    }

    @Override
    public double getBaseStrength() {
        return 450.0;
    }
}

