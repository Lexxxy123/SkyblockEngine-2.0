package in.godspunky.skyblock.item.armor;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.Sputnik;

public class BlyatHelmet implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "⚚ Gagarin's Space Helmet";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public double getBaseMagicFind() {
        return 100.0;
    }

    @Override
    public double getBaseDefense() {
        return 14261.0;
    }

    @Override
    public double getBaseStrength() {
        return 4500.0;
    }

    @Override
    public double getBaseFerocity() {
        return 250.0;
    }

    @Override
    public String getLore() {
        return Sputnik.trans("&8Authentic &c✯ USSR &8Product &81945/10 &8quality.");
    }
}
