package vn.giakhanhvn.skysim.item.oddities;

import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;

public class RefinedPowder implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName() {
        return "Refined Powder";
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
    public boolean isStackable() {
        return false;
    }
    
    @Override
    public String getLore() {
        return Sputnik.trans("Smell like... fish?");
    }
}
