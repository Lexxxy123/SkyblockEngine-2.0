package net.hypixel.skyblock.item.orb;

import net.hypixel.skyblock.user.PlayerStatistics;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.user.PlayerUtils;

public class QuantumfluxPowerOrb extends PowerOrb {
    @Override
    public String getAbilityDescription() {
        return "Place an orb for " + ChatColor.GREEN + "60s " + ChatColor.GRAY + "buffing up to " + ChatColor.AQUA + "5 " + ChatColor.GRAY + "players within " + ChatColor.GREEN + "18 " + ChatColor.GRAY + "blocks. " + ChatColor.DARK_GRAY + "Costs " + ChatColor.DARK_GRAY + "50% of max mana. " + ChatColor.DARK_GRAY + "Only " + ChatColor.DARK_GRAY + "one orb applies per player.";
    }

    @Override
    public String getURL() {
        return "6201ae1a8a04df52656f5e4813e1fbcf97877dbbfbc4268d04316d6f9f753";
    }

    @Override
    public String getDisplayName() {
        return "Quantumflux Power Orb";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public String getBuffName() {
        return "Quantumflux";
    }

    @Override
    public String getBuffDescription() {
        return "Grants " + ChatColor.AQUA + "+200% " + ChatColor.GRAY + "base mana regen. Heals " + ChatColor.RED + "5% " + ChatColor.GRAY + "of max " + ChatColor.RED + "❤ " + ChatColor.GRAY + "per second. Increases all heals by " + ChatColor.GREEN + "10%" + ChatColor.GRAY + ". Grants " + ChatColor.RED + "+125 " + ChatColor.RED + "❁ Strength";
    }

    @Override
    public String getCustomOrbName() {
        return "" + ChatColor.RED + ChatColor.BOLD + "Quantumflux";
    }

    @Override
    protected void buff(final Player player) {
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + player.getMaxHealth() * 0.05));
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        PlayerUtils.boostPlayer(statistics, new PlayerBoostStatistics() {
            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public Rarity getRarity() {
                return null;
            }

            @Override
            public GenericItemType getType() {
                return null;
            }

            @Override
            public double getBaseStrength() {
                return 125.0;
            }
        }, 20L);
        if (!player.hasMetadata("NPC")) {
            statistics.boostManaRegeneration(2.0, 20L);
            statistics.boostHealthRegeneration(0.1, 20L);
        }
    }

    @Override
    protected long getOrbLifeTicks() {
        return 1200L;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    protected void playEffect(final Location location) {
        location.getWorld().spigot().playEffect(location, Effect.COLOURED_DUST, 0, 1, 0.4f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
    }
}
