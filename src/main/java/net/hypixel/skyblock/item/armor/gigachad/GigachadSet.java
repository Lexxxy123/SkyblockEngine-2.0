package net.hypixel.skyblock.item.armor.gigachad;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.util.Sputnik;

public class GigachadSet implements ArmorSet {
    @Override
    public String getName() {
        return "Gigablood";
    }

    @Override
    public String getDescription() {
        return Sputnik.trans("&7Increase most of your stats by &a20% &7since you're a true &cgigachad&7. Beautiful, my comrade!");
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return GigachadHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return GigachadChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return GigachadLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return GigachadBoots.class;
    }
}
