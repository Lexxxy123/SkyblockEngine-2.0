/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.minichad;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.armor.minichad.MinichadBoots;
import net.hypixel.skyblock.item.armor.minichad.MinichadChestplate;
import net.hypixel.skyblock.item.armor.minichad.MinichadHelmet;
import net.hypixel.skyblock.item.armor.minichad.MinichadLeggings;
import net.hypixel.skyblock.util.Sputnik;

public class MinichadSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Minichad";
    }

    @Override
    public String getDescription() {
        return Sputnik.trans("&7Increase most of your stats by &a10%&7. Beautiful, my little comrade!");
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return MinichadHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return MinichadChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return MinichadLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return MinichadBoots.class;
    }
}

