package vn.giakhanhvn.skysim.item.pet;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.skill.CombatSkill;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.item.Rarity;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

import vn.giakhanhvn.skysim.util.Sputnik;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.List;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;

import java.util.Map;

public class GoldenTigerPet extends Pet {
    public static final Map<Player, Boolean> COOLDOWN;

    @Override
    public List<PetAbility> getPetAbilities(final SItem instance) {
        final int level = Pet.getLevel(instance);
        final BigDecimal fero = new BigDecimal(level * 0.1).setScale(1, RoundingMode.HALF_EVEN);
        final BigDecimal buffstat = new BigDecimal(level * 0.1).setScale(1, RoundingMode.HALF_EVEN);
        final BigDecimal buffstat2 = new BigDecimal(level * 0.05).setScale(1, RoundingMode.HALF_EVEN);
        final BigDecimal buffstat3 = new BigDecimal(level * 0.1).setScale(1, RoundingMode.HALF_EVEN);
        final List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Lucky New Year";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Arrays.asList(Sputnik.trans("&7Grant &e1% &7of your &c❁ Strength"), Sputnik.trans("&7as &b✯ Magic Find"));
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.EPIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Atrocities";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Arrays.asList(Sputnik.trans("&7For every &c30⫽ Ferocity&7,"), Sputnik.trans("&7adds &c" + fero.toPlainString() + " &7base damage"), ChatColor.GRAY + "to your weapons.");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Reinforced Body";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Collections.singletonList("Immune to any type of knockback.");
                }

                @Override
                public void onHurt(final EntityDamageByEntityEvent e, final Entity damager) {
                    final Entity en = e.getEntity();
                    final Vector v = new Vector(0, 0, 0);
                    SUtil.delay(() -> en.setVelocity(v), 0L);
                }
            });
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "King of the Jungle";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Collections.singletonList("Increases most stats by " + ChatColor.GREEN + buffstat.toPlainString() + "%");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.MYTHIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Golden Scaling";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Arrays.asList(Sputnik.trans("&7For each digit in your &bBits"), Sputnik.trans("&bpurse &7gain &c+" + buffstat3 + "⫽ Ferocity"), Sputnik.trans("&7and &b+" + buffstat2 + "✯ Magic Find"));
                }
            });
        }
        return abilities;
    }

    @Override
    public Skill getSkill() {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL() {
        return "f547356d9b9bc9cf87da951fedd705e9388969bf3743c9ee63becf7e743a7c95";
    }

    @Override
    public String getDisplayName() {
        return "Golden Tiger";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerDefense() {
        return 0.0;
    }

    @Override
    public double getPerTrueDefense() {
        return 0.0;
    }

    @Override
    public double getPerStrength() {
        return 2.0;
    }

    @Override
    public double getPerFerocity() {
        return 0.5;
    }

    @Override
    public double getPerAttackSpeed() {
        return 0.2;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.92156863f, 0.8980392f, 0.20392157f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.9882353f, 0.7294118f, 0.011764706f, 1.0f, 0, 64);
    }

    static {
        COOLDOWN = new HashMap<Player, Boolean>();
    }
}
