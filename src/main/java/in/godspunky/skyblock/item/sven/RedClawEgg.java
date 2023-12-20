package in.godspunky.skyblock.item.sven;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;

public class RedClawEgg implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Red Claw Egg";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }
}
