/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.dragon.superior;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.dragon.superior.SuperiorDragonBoots;
import net.hypixel.skyblock.item.dragon.superior.SuperiorDragonChestplate;
import net.hypixel.skyblock.item.dragon.superior.SuperiorDragonHelmet;
import net.hypixel.skyblock.item.dragon.superior.SuperiorDragonLeggings;

public class SuperiorDragonSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Superior Blood";
    }

    @Override
    public String getDescription() {
        return "All your stats are increased by 5% and Aspect of the Dragons ability deals 50% more Ability Damage.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return SuperiorDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return SuperiorDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return SuperiorDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return SuperiorDragonBoots.class;
    }
}

