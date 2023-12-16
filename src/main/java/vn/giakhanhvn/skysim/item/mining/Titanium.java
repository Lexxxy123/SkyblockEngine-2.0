package vn.giakhanhvn.skysim.item.mining;

import vn.giakhanhvn.skysim.item.*;
import vn.giakhanhvn.skysim.util.Sputnik;

public class Titanium implements MaterialStatistics, MaterialFunction, SkullStatistics {
    @Override
    public String getDisplayName() {
        return "Titanium";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public String getLore() {
        return Sputnik.trans("&7&oIts strength and resistance to corrosion are the highest of any metallic element.");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public String getURL() {
        return "a14c6e41a762d37863a9fff6888c738905b92cc6c3898892a38dfdfe2ac4bf";
    }
}
