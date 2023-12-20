package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;

public class GameBreaker implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Game Breaker";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
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
        return "This item was given to a player who reported a game breaking exploit. What a guy!";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
