/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Skyblock is licensed under the Creative Commons Non-Commercial 4.0 International License.
 *
 * You may not use this software for commercial use, however you are free
 * to modify, copy, redistribute, or build upon our codebase. You must give
 * appropriate credit, provide a link to the license, and indicate
 * if changes were made.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, visit https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */
package net.hypixel.skyblock.item.armor.enderman;



import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class VoidbaneChestplate implements ToolStatistics, MaterialFunction, LeatherArmorStatistics {
    @Override
    public String getDisplayName() {
        return "Corrupted Voidbane Chestplate";
    }

    @Override
    public double getBaseStrength() {
        return 65;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public double getBaseHealth() {
        return 255;
    }

    @Override
    public double getBaseDefense() {
        return 415;
    }

    @Override
    public List<String> killReplacementLore() {
        final List<String> lore = new ArrayList<>();
        lore.add(formatLore("&8Tiered Bonus: Void Conquer"));
        lore.add(formatLore("While sneaking in the"));
        lore.add(formatLore("&9End Dimension&7, gain &a45%"));
        lore.add(formatLore("&7damage against enderman per"));
        lore.add(formatLore("piece of this armor worn."));
        lore.add(" ");
        lore.add(formatLore("&6Full Set Bonus: Rebound"));
        lore.add(formatLore("All damage you receive from"));
        lore.add(formatLore("Enderman is &areflected&7 back"));
        lore.add(formatLore("after &a3s&7 if you are above &a50%"));
        lore.add(formatLore("health. The amount of times this ability"));
        lore.add(formatLore("activates scales with Bonus Defense."));
        lore.add(formatLore(" "));
        lore.add(formatLore("&6Piece Bonus: Enderman Bulwark"));
        lore.add(formatLore("Kill Enderman to accumulate"));
        lore.add(formatLore("more Defense against them."));
        lore.add(formatLore("Piece Bonus: &a+<SKYBLOCK_BONUS_DEFENSE>❈&7"));
        lore.add(formatLore("Next Upgrade: &a+<SKYBLOCK_NEXT_DEFENSE>❈&8 (&a<SKYBLOCK_CURRENT_KILLS>&7/&c<SKYBLOCK_REQUIRED_KILLS>&8)"));

        return lore;
    }

    private String formatLore(String lore) {
        return ChatColor.translateAlternateColorCodes('&', lore);
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public int getColor() {
        return 0xA0011;
    }

}