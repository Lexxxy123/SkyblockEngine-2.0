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

public class LynxTalisman
implements AccessoryStatistics,
MaterialFunction {
    private static final Map<UUID, Integer> HITS = new HashMap<UUID, Integer>();

    @Override
    public String getURL() {
        return "f06706eecb2d558ace27abda0b0b7b801d36d17dd7a890a9520dbe522374f8a6";
    }

    @Override
    public String getDisplayName() {
        return "Lynx Talisman";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public double getBaseSpeed() {
        return 0.02;
    }
}

