/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.dragon.protector;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.dragon.protector.ProtectorDragonBoots;
import net.hypixel.skyblock.item.dragon.protector.ProtectorDragonChestplate;
import net.hypixel.skyblock.item.dragon.protector.ProtectorDragonHelmet;
import net.hypixel.skyblock.item.dragon.protector.ProtectorDragonLeggings;

public class ProtectorDragonSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Protective Blood";
    }

    @Override
    public String getDescription() {
        return "Increases the Defense bonus of each armor piece by 1% for each percent of missing Health.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return ProtectorDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return ProtectorDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return ProtectorDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return ProtectorDragonBoots.class;
    }
}

