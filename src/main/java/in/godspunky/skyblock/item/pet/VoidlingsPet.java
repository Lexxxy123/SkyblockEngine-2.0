package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.features.skill.CombatSkill;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.util.Groups;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.RarityValue;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.util.Sputnik;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoidlingsPet extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        RarityValue<Double> enderianMul = new RarityValue<Double>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        RarityValue<Double> savvyMul = new RarityValue<Double>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        int level = getLevel(instance);
        BigDecimal enderian = new BigDecimal(level * enderianMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal savvy = new BigDecimal(level * savvyMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal zealot = new BigDecimal(level * 0.25).setScale(2, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Voidling's Summoner";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Arrays.asList("Gives you a chance to summon " + ChatColor.DARK_PURPLE + "Voidling's", ChatColor.DARK_PURPLE + "Altars " + ChatColor.GRAY + "while killing " + ChatColor.LIGHT_PURPLE + "Voidling Extremist", Sputnik.trans("&4☠ &cRequires &5Enderman Slayer 6."));
            }

            @Override
            public void onHurt(EntityDamageByEntityEvent e, Entity damager) {
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.RARE)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Ender's Stronghold";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    BigDecimal e = new BigDecimal(0.3 * level).setScale(1, RoundingMode.HALF_EVEN);
                    return Collections.singletonList(Sputnik.trans("&7Take &a" + e.toPlainString() + "% &7less damage from ender mobs."));
                }

                @Override
                public void onHurt(EntityDamageByEntityEvent e, Entity damager) {
                    SEntity entity = SEntity.findSEntity(damager);
                    if (null == entity) {
                        return;
                    }
                    if (Groups.END_MOBS.contains(entity.getSpecType())) {
                        e.setDamage(e.getDamage() - e.getDamage() * (0.3 * level) / 100.0);
                    }
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
        return "99e01b3a35ae3be4483b6df69b7070bd6dc75b399d7e2ebbc7b8840332f7b3a0";
    }

    @Override
    public String getDisplayName() {
        return "Voidling's Micron";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public double getPerCritDamage() {
        return 0.01;
    }

    @Override
    public double getPerStrength() {
        return 2.0;
    }

    @Override
    public double getPerTrueDefense() {
        return 0.15;
    }

    @Override
    public void particleBelowA(Player p, Location l) {
        p.spigot().playEffect(l, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        p.spigot().playEffect(l, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        p.spigot().playEffect(l, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }
}
