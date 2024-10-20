/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.accessory;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.accessory.AccessoryFunction;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import org.bukkit.ChatColor;

public class DanteTalisman
implements AccessoryStatistics,
AccessoryFunction {
    @Override
    public String getURL() {
        return "cf92982f1a302310643a20ce51623f8199b7545e70dc6b93ed6bd61dc42ff213";
    }

    @Override
    public String getDisplayName() {
        return "Dante Talisman";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }

    @Override
    public List<String> getListLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Official medal proving your");
        lore.add(ChatColor.GRAY + "eternal support for Dante.");
        lore.add(ChatColor.GRAY + "&7");
        lore.add(ChatColor.GRAY + "When damaged, you");
        lore.add(ChatColor.GRAY + "occasionally spit straight");
        lore.add(ChatColor.GRAY + "facts into the chat.");
        lore.add(ChatColor.GRAY + "&7");
        lore.add(ChatColor.DARK_GRAY + "'dante best' - Goons, 2021 - 2021");
        return lore;
    }
}

