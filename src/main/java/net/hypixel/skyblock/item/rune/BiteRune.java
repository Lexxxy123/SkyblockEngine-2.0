package net.hypixel.skyblock.item.rune;

import org.bukkit.ChatColor;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.Rune;
import net.hypixel.skyblock.item.SpecificItemType;

public class BiteRune implements Rune {
    @Override
    public String getDisplayName() {
        return ChatColor.GREEN + "◆ Bite Rune";
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
    public SpecificItemType getSpecificType() {
        return SpecificItemType.COSMETIC;
    }

    @Override
    public String getURL() {
        return "43a1ad4fcc42fb63c681328e42d63c83ca193b333af2a426728a25a8cc600692";
    }
}
