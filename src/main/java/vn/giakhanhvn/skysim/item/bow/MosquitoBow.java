package vn.giakhanhvn.skysim.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.Repeater;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.item.*;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.util.DefenseReplacement;
import vn.giakhanhvn.skysim.util.ManaReplacement;
import vn.giakhanhvn.skysim.util.SUtil;

public class MosquitoBow implements ToolStatistics, BowFunction, Ability {
    @Override
    public String getAbilityName() {
        return "Nasty Bite";
    }

    @Override
    public String getAbilityDescription() {
        return "Fully charged shots while sneaking. Costs " + ChatColor.AQUA + "11%  " + ChatColor.GRAY + "of max Mana, deals " + ChatColor.RED + "+19% " + ChatColor.GRAY + "more damage, heal for " + ChatColor.GREEN + "2x " + ChatColor.GRAY + "the Mana cost";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Mosquito Bow";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOW;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public int getBaseDamage() {
        return 251;
    }

    @Override
    public double getBaseStrength() {
        return 151.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.39;
    }

    @Override
    public void onBowShoot(final SItem bow, final EntityShootBowEvent e) {
        final Player player = (Player) e.getEntity();
        if (!player.isSneaking()) {
            return;
        }
        if (e.getForce() != 1.0f) {
            return;
        }
        final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100.0);
        final int cost = PlayerUtils.getFinalManaCost(player, bow, (int) (manaPool * 0.11));
        final boolean take = PlayerUtils.takeMana(player, cost);
        if (!take) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            final long c = System.currentTimeMillis();
            Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement() {
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
        Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement() {
            @Override
            public String getReplacement() {
                return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + MosquitoBow.this.getAbilityName() + ChatColor.AQUA + ")";
            }

            @Override
            public long getEnd() {
                return c + 2000L;
            }
        });
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + manaPool * 0.11 * 2.0));
        e.getProjectile().setMetadata("bite", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
    }

    @Override
    public void onBowHit(final Entity hit, final Player shooter, final Arrow arrow, final SItem weapon, final AtomicDouble finalDamage) {
        if (!arrow.hasMetadata("bite")) {
            return;
        }
        finalDamage.set(finalDamage.get() + finalDamage.get() * 0.19);
    }
}
