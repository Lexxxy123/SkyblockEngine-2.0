package in.godspunky.skyblock.item.shovel.vanilla;

import in.godspunky.skyblock.item.*;

public class StoneShovel implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Stone Shovel";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage() {
        return 20;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.TOOL;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SHOVEL;
    }
}
