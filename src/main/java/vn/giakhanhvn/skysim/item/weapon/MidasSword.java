package vn.giakhanhvn.skysim.item.weapon;

import vn.giakhanhvn.skysim.item.*;

public class MidasSword implements ToolStatistics, MaterialFunction {
    @Override
    public int getBaseDamage() {
        return 50;
    }

    @Override
    public double getBaseSpeed() {
        return 0.4;
    }

    @Override
    public String getDisplayName() {
        return "Midas Test";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public boolean displayCoins() {
        return true;
    }
}
