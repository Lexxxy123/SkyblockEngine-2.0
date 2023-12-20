package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.item.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.util.DefenseReplacement;
import in.godspunky.skyblock.util.ManaReplacement;
import in.godspunky.skyblock.util.SUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EdibleMace implements ToolStatistics, MaterialFunction, Ability {
    public static final Map<UUID, Boolean> edibleMace;

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
    public void onAbilityUse(final Player player1, final SItem sItem) {
        if (!EdibleMace.edibleMace.containsKey(player1.getUniqueId())) {
            EdibleMace.edibleMace.put(player1.getUniqueId(), false);
        }
        if (EdibleMace.edibleMace.containsKey(player1.getUniqueId())) {
            if (!EdibleMace.edibleMace.get(player1.getUniqueId())) {
                final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId()).getIntelligence().addAll() + 100.0);
                final int cost = PlayerUtils.getFinalManaCost(player1, sItem, 100);
                final boolean take = PlayerUtils.takeMana(player1, cost);
                if (!take) {
                    player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                    final long c = System.currentTimeMillis();
                    Repeater.MANA_REPLACEMENT_MAP.put(player1.getUniqueId(), new ManaReplacement() {
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
                Repeater.DEFENSE_REPLACEMENT_MAP.put(player1.getUniqueId(), new DefenseReplacement() {
                    @Override
                    public String getReplacement() {
                        return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + EdibleMace.this.getAbilityName() + ChatColor.AQUA + ")";
                    }

                    @Override
                    public long getEnd() {
                        return c + 2000L;
                    }
                });
                EdibleMace.edibleMace.put(player1.getUniqueId(), true);
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

    static {
        edibleMace = new HashMap<UUID, Boolean>();
    }
}
