/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.armor.enderman;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;
import org.bukkit.ChatColor;

public class VoidbaneChestplate
implements ToolStatistics,
MaterialFunction,
LeatherArmorStatistics {
    @Override
    public String getDisplayName() {
        return "Corrupted Voidbane Chestplate";
    }

    @Override
    public double getBaseStrength() {
        return 65.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public double getBaseHealth() {
        return 255.0;
    }

    @Override
    public double getBaseDefense() {
        return 415.0;
    }

    @Override
    public List<String> killReplacementLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(this.formatLore("&8Tiered Bonus: Void Conquer"));
        lore.add(this.formatLore("While sneaking in the"));
        lore.add(this.formatLore("&9End Dimension&7, gain &a45%"));
        lore.add(this.formatLore("&7damage against enderman per"));
        lore.add(this.formatLore("piece of this armor worn."));
        lore.add(" ");
        lore.add(this.formatLore("&6Full Set Bonus: Rebound"));
        lore.add(this.formatLore("All damage you receive from"));
        lore.add(this.formatLore("Enderman is &areflected&7 back"));
        lore.add(this.formatLore("after &a3s&7 if you are above &a50%"));
        lore.add(this.formatLore("health. The amount of times this ability"));
        lore.add(this.formatLore("activates scales with Bonus Defense."));
        lore.add(this.formatLore(" "));
        lore.add(this.formatLore("&6Piece Bonus: Enderman Bulwark"));
        lore.add(this.formatLore("Kill Enderman to accumulate"));
        lore.add(this.formatLore("more Defense against them."));
        lore.add(this.formatLore("Piece Bonus: &a+<SKYBLOCK_BONUS_DEFENSE>\u2748&7"));
        lore.add(this.formatLore("Next Upgrade: &a+<SKYBLOCK_NEXT_DEFENSE>\u2748&8 (&a<SKYBLOCK_CURRENT_KILLS>&7/&c<SKYBLOCK_REQUIRED_KILLS>&8)"));
        return lore;
    }

    private String formatLore(String lore) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)lore);
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public int getColor() {
        return 655377;
    }
}

