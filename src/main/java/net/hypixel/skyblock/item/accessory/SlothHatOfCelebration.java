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


import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;

import java.util.*;

public class SlothHatOfCelebration implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Sloth Hat of Celebration";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.01;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(
                Sputnik.trans4( "&6Ability: Party Time",
                "&7Gain &b+1 Intelligence&7 while",
                "&7on your head for each SkyBlock",
                "&7year you've been playing.")
        );
    }

    @Override
    public String getURL() {
        return "70bfaf5ab3f817fbed7263fc1c150b571f01b2f7ef17bc579610a495bc4cfee9";
    }
}
