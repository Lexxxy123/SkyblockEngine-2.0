package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.util.Sputnik;

public class GyrokineticEye implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Gyrokinetic Eye";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
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
    public boolean isStackable() {
        return false;
    }

    @Override
    public String getLore() {
        return Sputnik.trans("The eye of a deep dark enemy, can absorb any other living creatures.");
    }
}
