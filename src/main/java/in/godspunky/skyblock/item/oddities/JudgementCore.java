package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class JudgementCore implements MaterialStatistics, MaterialFunction, SkullStatistics {
    @Override
    public String getDisplayName() {
        return "Judgement Core";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return false;
    }

    @Override
    public String getURL() {
        return "2f3ddd7f81089c85b26ed597675519f03a1dcd6d1713e0cfc66afb8743cbe0";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
