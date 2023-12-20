package in.godspunky.skyblock.item.rune;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.Rune;
import in.godspunky.skyblock.item.SpecificItemType;
import org.bukkit.ChatColor;

public class SnakeRune implements Rune {
    @Override
    public String getDisplayName() {
        return ChatColor.GREEN + "◆ Snake Rune";
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
    public SpecificItemType getSpecificType() {
        return SpecificItemType.COSMETIC;
    }

    @Override
    public String getURL() {
        return "2c4a65c689b2d36409100a60c2ab8d3d0a67ce94eea3c1f7ac974fd893568b5d";
    }
}
