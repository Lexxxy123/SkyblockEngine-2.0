package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.potion.PotionEffect;
import in.godspunky.skyblock.potion.PotionEffectType;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.ArrayList;

public class DiamondSadanTrophy implements SkullStatistics, MaterialFunction, Ability, Ownable {
    private boolean bool;

    public DiamondSadanTrophy() {
        this.bool = true;
    }

    @Override
    public String getURL() {
        return "62e5ff38849a677e51a9acb7bc6c398af973ae218a0312352ae060c79609d86";
    }

    @Override
    public String getDisplayName() {
        return "Diamond Sadan Trophy";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getAbilityName() {
        return "Ancient's Bless";
    }

    @Override
    public String getAbilityDescription() {
        return Sputnik.trans("&7Cast a blessing spell which will give you &91 hour &7of &ceven more &coverpowered &7potion effects");
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 72000;
    }

    @Override
    public int getManaCost() {
        return -1;
    }

    @Override
    public boolean displayUsage() {
        return true;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public boolean requirementsUse(final Player player, final SItem sItem) {
        return User.getUser(player.getUniqueId()).getBCollection() < 1000L;
    }

    @Override
    public String getAbilityReq() {
        return "&cYou need at least 1,000 Sadan Kills to use this item!";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        SUtil.delay(() -> this.bool = false, 35L);
        final ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        effects.add(new PotionEffect(PotionEffectType.FEROCITY, 10, 72000L));
        effects.add(new PotionEffect(PotionEffectType.ARCHERY, 6, 72000L));
        effects.add(new PotionEffect(PotionEffectType.CRITICAL, 14, 72000L));
        effects.add(new PotionEffect(PotionEffectType.STRENGTH, 20, 72000L));
        if (!player.getWorld().getName().contains("arena")) {
            effects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 4, 1728000L));
            effects.add(new PotionEffect(PotionEffectType.RABBIT, 6, 1728000L));
        }
        effects.add(new PotionEffect(PotionEffectType.RESISTANCE, 20, 72000L));
        effects.add(new PotionEffect(PotionEffectType.SPEED, 11, 72000L));
        effects.add(new PotionEffect(PotionEffectType.HASTE, 4, 72000L));
        effects.add(new PotionEffect(PotionEffectType.SPIRIT, 8, 72000L));
        effects.add(new PotionEffect(PotionEffectType.TRUE_RESISTANCE, 7, 72000L));
        effects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 1, 72000L));
        effects.add(new PotionEffect(PotionEffectType.WATER_BREATH, 1, 72000L));
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1, 72000L));
        effects.add(new PotionEffect(PotionEffectType.STAMINA, 8, 72000L));
        effects.add(new PotionEffect(PotionEffectType.MAGIC_FIND, 15, 72000L));
        final User user = User.getUser(player.getUniqueId());
        new BukkitRunnable() {
            public void run() {
                if (!DiamondSadanTrophy.this.bool) {
                    player.sendMessage(Sputnik.trans("&a&lWHOOOSH! &eThe &cDiamond Sadan Trophy &egrants you even stronger super-powers for &91 hour&e!"));
                    player.getWorld().playEffect(player.getLocation().add(0.0, 0.0, 0.0), Effect.EXPLOSION_HUGE, 0);
                    this.cancel();
                    return;
                }
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.POTION_SWIRL, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
                player.getWorld().playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        for (final PotionEffect effect : effects) {
            user.removePotionEffect(effect.getType());
            PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
            if (effect.getType().getOnDrink() != null) {
                effect.getType().getOnDrink().accept(effect, player);
            }
            user.addPotionEffect(effect);
        }
    }
}
