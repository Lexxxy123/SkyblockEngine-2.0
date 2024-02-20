package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.*;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.user.PlayerUtils;

public class WeirdTuba implements MaterialStatistics, MaterialFunction, Ability {
    @Override
    public String getAbilityName() {
        return "Howl";
    }

    @Override
    public String getAbilityDescription() {
        return "You and 4 nearby players gain:";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 400;
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        player.getWorld().playSound(player.getLocation(), Sound.WOLF_HOWL, 1.0f, 1.0f);
        PlayerUtils.boostPlayer(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()), new PlayerBoostStatistics() {
            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public Rarity getRarity() {
                return null;
            }

            @Override
            public String getLore() {
                return null;
            }

            @Override
            public GenericItemType getType() {
                return null;
            }

            @Override
            public double getBaseSpeed() {
                return 0.3;
            }

            @Override
            public double getBaseStrength() {
                return 30.0;
            }
        }, 400L);
        for (final Entity e : player.getNearbyEntities(4.0, 4.0, 4.0)) {
            if (e instanceof LivingEntity && e instanceof Player) {
                PlayerUtils.boostPlayer(PlayerUtils.STATISTICS_CACHE.get(e.getUniqueId()), new PlayerBoostStatistics() {
                    @Override
                    public String getDisplayName() {
                        return null;
                    }

                    @Override
                    public Rarity getRarity() {
                        return null;
                    }

                    @Override
                    public String getLore() {
                        return null;
                    }

                    @Override
                    public GenericItemType getType() {
                        return null;
                    }

                    @Override
                    public double getBaseSpeed() {
                        return 0.3;
                    }

                    @Override
                    public double getBaseStrength() {
                        return 30.0;
                    }
                }, 400L);
            }
        }
    }

    @Override
    public int getManaCost() {
        return 150;
    }

    @Override
    public String getDisplayName() {
        return "Weird Tuba";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
