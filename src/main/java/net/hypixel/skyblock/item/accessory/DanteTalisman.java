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
package net.hypixel.skyblock.item.accessory;


import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DanteTalisman implements AccessoryStatistics, AccessoryFunction {

    @Override
    public String getURL()
    {
        return "cf92982f1a302310643a20ce51623f8199b7545e70dc6b93ed6bd61dc42ff213";
    }

    @Override
    public String getDisplayName()
    {
        return "Dante Talisman";
    }

    @Override
    public Rarity getRarity()
    {
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
        final ArrayList<String> lore = new ArrayList<>();
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