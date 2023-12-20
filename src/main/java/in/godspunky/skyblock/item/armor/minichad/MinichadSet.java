package in.godspunky.skyblock.item.armor.minichad;

import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.armor.ArmorSet;
import in.godspunky.skyblock.util.Sputnik;

public class MinichadSet implements ArmorSet {
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
