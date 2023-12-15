package vn.giakhanhvn.skysim.item.weapon;

import vn.giakhanhvn.skysim.item.ShapedRecipe;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.Location;
import vn.giakhanhvn.skysim.item.PlayerBoostStatistics;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.PlayerStatistics;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.Material;

import java.util.Set;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;

import java.math.BigDecimal;

import vn.giakhanhvn.skysim.item.Ability;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class AspectOfTheEnd implements ToolStatistics, MaterialFunction, Ability {
    public BigDecimal a;

    @Override
    public int getBaseDamage() {
        return 100;
    }

    @Override
    public double getBaseStrength() {
        return 100.0;
    }

    @Override
    public String getDisplayName() {
        return "Aspect of the End";
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
        return "Instant Transmission";
    }

    @Override
    public String getAbilityDescription() {
        return "Teleports you " + ChatColor.GREEN + "8 blocks " + ChatColor.GRAY + "ahead and gain " + ChatColor.GREEN + "+50 " + ChatColor.WHITE + "✦ " + ChatColor.WHITE + "Speed " + ChatColor.GRAY + "for " + ChatColor.GREEN + "3 seconds.";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        try {
            int f_ = 8;
            for (int range = 1; range < 8; ++range) {
                final Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (location.getBlock().getType() != Material.AIR) {
                    f_ = range;
                    break;
                }
            }
            final Location location2 = player.getTargetBlock((Set) null, f_ - 1).getLocation();
            location2.setYaw(player.getLocation().getYaw());
            location2.setPitch(player.getLocation().getPitch());
            location2.add(0.5, 0.0, 0.5);
            if (f_ != 8) {
                player.sendMessage(ChatColor.RED + "There are blocks in the way!");
            }
            if (f_ > 1) {
                Sputnik.teleport(player, location2);
            } else {
                Sputnik.teleport(player, player.getLocation());
            }
        } catch (final IllegalStateException ex) {
        }
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0f, 1.0f);
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
                return 0.5;
            }
        }, 60L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public void load() {
        final ShapedRecipe recipe = new ShapedRecipe(SMaterial.ASPECT_OF_THE_END);
        recipe.shape("a", "b", "c");
        recipe.set('a', SMaterial.ENCHANTED_EYE_OF_ENDER, 16);
        recipe.set('b', SMaterial.ENCHANTED_EYE_OF_ENDER, 16);
        recipe.set('c', SMaterial.ENCHANTED_DIAMOND);
    }
}
