package vn.giakhanhvn.skysim.reforge;

import java.util.Arrays;
import vn.giakhanhvn.skysim.item.GenericItemType;
import java.util.List;
import vn.giakhanhvn.skysim.item.RarityValue;

public interface Reforge
{
    String getName();
    
    default RarityValue<Double> getStrength() {
        return RarityValue.zeroDouble();
    }
    
    default RarityValue<Double> getCritChance() {
        return RarityValue.zeroDouble();
    }
    
    default RarityValue<Double> getCritDamage() {
        return RarityValue.zeroDouble();
    }
    
    default RarityValue<Double> getIntelligence() {
        return RarityValue.zeroDouble();
    }
    
    default RarityValue<Double> getFerocity() {
        return RarityValue.zeroDouble();
    }
    
    default RarityValue<Double> getAttackSpeed() {
        return RarityValue.zeroDouble();
    }
    
    default List<GenericItemType> getCompatibleTypes() {
        return Arrays.<GenericItemType>asList(GenericItemType.values());
    }
    
    static Reforge blank() {
        return () -> "Blank";
    }
}
