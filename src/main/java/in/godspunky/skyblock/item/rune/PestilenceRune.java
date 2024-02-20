package in.godspunky.skyblock.item.rune;

import org.bukkit.ChatColor;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.Rune;
import in.godspunky.skyblock.item.SpecificItemType;

public class PestilenceRune implements Rune {
    @Override
    public String getDisplayName() {
        return ChatColor.DARK_GREEN + "◆ Pestilence Rune";
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
    public SpecificItemType getSpecificType() {
        return SpecificItemType.COSMETIC;
    }

    @Override
    public String getURL() {
        return "a8c4811395fbf7f620f05cc3175cef1515aaf775ba04a01045027f0693a90147";
    }
}
