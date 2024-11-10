/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.weapon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.DefenseReplacement;
import net.hypixel.skyblock.util.ManaReplacement;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EdibleMace
implements ToolStatistics,
MaterialFunction,
Ability {
    public static final Map<UUID, Boolean> edibleMace = new HashMap<UUID, Boolean>();

    @Override
    public int getBaseDamage() {
        return 125;
    }

    @Override
    public double getBaseStrength() {
        return 25.0;
    }

    @Override
    public String getDisplayName() {
        return "Edible Mace";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "ME SMASH HEAD";
    }

    @Override
    public String getAbilityDescription() {
        return "Your next attack deals " + ChatColor.RED + "double " + ChatColor.RED + "damage " + ChatColor.GRAY + "and weakens animals, making them deal " + ChatColor.RED + "-35% " + ChatColor.GRAY + "damage for " + ChatColor.GREEN + "30 " + ChatColor.GRAY + "seconds.";
    }

    @Override
    public void onAbilityUse(Player player1, SItem sItem) {
        if (!edibleMace.containsKey(player1.getUniqueId())) {
            edibleMace.put(player1.getUniqueId(), false);
        }
        if (edibleMace.containsKey(player1.getUniqueId())) {
            if (!edibleMace.get(player1.getUniqueId()).booleanValue()) {
                int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId()).getIntelligence().addAll() + 100.0);
                final int cost = PlayerUtils.getFinalManaCost(player1, sItem, 100);
                boolean take = PlayerUtils.takeMana(player1, cost);
                if (!take) {
                    player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                    final long c = System.currentTimeMillis();
                    Repeater.MANA_REPLACEMENT_MAP.put(player1.getUniqueId(), new ManaReplacement(){

                        @Override
                        public String getReplacement() {
                            return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                        }

                        @Override
                        public long getEnd() {
                            return c + 1500L;
                        }
                    });
                    return;
                }
                final long c = System.currentTimeMillis();
                Repeater.DEFENSE_REPLACEMENT_MAP.put(player1.getUniqueId(), new DefenseReplacement(){

                    @Override
                    public String getReplacement() {
                        return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + EdibleMace.this.getAbilityName() + ChatColor.AQUA + ")";
                    }

                    @Override
                    public long getEnd() {
                        return c + 2000L;
                    }
                });
                edibleMace.put(player1.getUniqueId(), true);
            } else {
                player1.sendMessage(ChatColor.RED + "The ability is already active!");
            }
        }
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }
}

