/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.dragon.wise;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.dragon.wise.WiseDragonBoots;
import net.hypixel.skyblock.item.dragon.wise.WiseDragonChestplate;
import net.hypixel.skyblock.item.dragon.wise.WiseDragonHelmet;
import net.hypixel.skyblock.item.dragon.wise.WiseDragonLeggings;

public class WiseDragonSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Wise Blood";
    }

    @Override
    public String getDescription() {
        return "All abilities cost 1/3 less Mana.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return WiseDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return WiseDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return WiseDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return WiseDragonBoots.class;
    }
}

