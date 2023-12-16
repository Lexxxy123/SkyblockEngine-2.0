package vn.giakhanhvn.skysim.item.tarantula;

import vn.giakhanhvn.skysim.item.*;

public class ToxicArrowPoison implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Toxic Arrow Poison";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.ARROW_POISON;
    }
}
