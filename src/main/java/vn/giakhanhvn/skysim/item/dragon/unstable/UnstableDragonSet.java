package vn.giakhanhvn.skysim.item.dragon.unstable;

import vn.giakhanhvn.skysim.item.MaterialStatistics;
import vn.giakhanhvn.skysim.item.armor.ArmorSet;

public class UnstableDragonSet implements ArmorSet {
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
