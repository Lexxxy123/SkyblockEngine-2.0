/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.dragon.unstable;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.dragon.unstable.UnstableDragonBoots;
import net.hypixel.skyblock.item.dragon.unstable.UnstableDragonChestplate;
import net.hypixel.skyblock.item.dragon.unstable.UnstableDragonHelmet;
import net.hypixel.skyblock.item.dragon.unstable.UnstableDragonLeggings;

public class UnstableDragonSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Unstable Blood";
    }

    @Override
    public String getDescription() {
        return "Every 10 seconds, strike nearby mobs within a 7 block radius dealing 3,000 Damage!";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return UnstableDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return UnstableDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return UnstableDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return UnstableDragonBoots.class;
    }
}

