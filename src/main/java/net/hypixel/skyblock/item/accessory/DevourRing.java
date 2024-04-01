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


import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class DevourRing implements AccessoryStatistics, AccessoryFunction {
    private static final Map<UUID, Integer> HITS = new HashMap<>();

    @Override
    public String getURL()
    {
        return "f06706eecb2d558ace27abda0b0b7b801d36d17dd7a890a9520dbe522374f8a6";
    }

    @Override
    public List<String> getListLore() {
        final ArrayList<String> lore = new ArrayList<>();

        lore.add(ChatColor.GRAY + "Heal 5❤ when killing a");
        lore.add(ChatColor.GRAY + "monster.");
        return lore;
    }

    @Override
    public String getDisplayName()
    {
        return "Devour Ring";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.RARE;
    }

    @Override
    public void onDamageInInventory(SItem weapon, Player player, Entity damaged, SItem accessory, AtomicDouble damage) {
        player.setMaxHealth(Math.min(player.getMaxHealth(), player.getHealth() + (player.getMaxHealth() * 0.02)));
    }
}