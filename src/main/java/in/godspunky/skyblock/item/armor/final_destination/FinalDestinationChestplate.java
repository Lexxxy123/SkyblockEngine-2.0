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
package in.godspunky.skyblock.item.armor.final_destination;


import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.armor.LeatherArmorStatistics;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class FinalDestinationChestplate implements ToolStatistics, MaterialFunction, LeatherArmorStatistics {
    @Override
    public String getDisplayName() {
        return "Final Destination Chestplate";
    }

    @Override
    public List<String> getListLore() {
        final List<String> lore = new ArrayList<>();
        lore.add(formatLore("&6Full Set Bonus: Vivacious Darkness"));
        lore.add(formatLore("Costs &32 Soulflow&7 per 5s"));
        lore.add(formatLore("&7in combat while &asneaking&7:"));
        lore.add(formatLore("&3> &c+30 Strength"));
        lore.add(formatLore("&3> &e+20 Bonus Attack Speed"));
        lore.add(formatLore("&3> &f+10 Speed"));
        lore.add(formatLore("&3> &7Multiply &bIntelligence&7 by &b1.25x&7"));
        lore.add(formatLore("&3> &c+200 Ferocity&7 against Endermen"));
        lore.add(formatLore("&3> &a+100%&7 damage against Endermen"));
        return lore;
    }

    @Override
    public List<String> killReplacementLore() {
        final List<String> lore = new ArrayList<>();
        lore.add(formatLore("&6Piece Bonus: Enderman Bulwark"));
        lore.add(formatLore("&7Kill endermen to accumulate"));
        lore.add(formatLore("&7defense against them."));
        lore.add(formatLore("&7Piece Bonus: &a+<SKYBLOCK_BONUS_DEFENSE>❈&7"));
        lore.add(formatLore("&7Next Upgrade: &a+<SKYBLOCK_NEXT_DEFENSE>❈&8 (&a<SKYBLOCK_CURRENT_KILLS>&7/&c<SKYBLOCK_REQUIRED_KILLS>&8)"));
        return lore;
    }

    private String formatLore(String lore) {
        return ChatColor.translateAlternateColorCodes('&', lore);
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
    public double getBaseHealth() {
        return 200;
    }

    @Override
    public double getBaseDefense() {
        return 100;
    }

    @Override
    public double getBaseIntelligence() {
        return 100;
    }


    @Override
    public int getColor() {
        return 0xA0011;
    }


}