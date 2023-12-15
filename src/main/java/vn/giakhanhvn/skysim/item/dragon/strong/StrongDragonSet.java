package vn.giakhanhvn.skysim.item.dragon.strong;

import vn.giakhanhvn.skysim.item.MaterialStatistics;
import vn.giakhanhvn.skysim.item.armor.ArmorSet;

public class StrongDragonSet implements ArmorSet {
    @Override
    public String getName() {
        return "Strong Blood";
    }

    @Override
    public String getDescription() {
        return "Improves the Aspect of the End: +75 Base Damage, +2 Teleport distance, +3 seconds of speed, Strength on ability";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return StrongDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return StrongDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return StrongDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return StrongDragonBoots.class;
    }
}
