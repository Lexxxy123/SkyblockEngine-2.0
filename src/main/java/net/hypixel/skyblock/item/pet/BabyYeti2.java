package net.hypixel.skyblock.item.pet;

import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.RarityValue;
import net.hypixel.skyblock.item.SItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BabyYeti2 extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        RarityValue<Double> enderianMul = new RarityValue<Double>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        RarityValue<Double> savvyMul = new RarityValue<Double>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        int level = getLevel(instance);
        BigDecimal coldbreeze = new BigDecimal(level * 0.5).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal yeti1 = new BigDecimal(level).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal yeti2 = new BigDecimal(level).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal iceshield = new BigDecimal(level).setScale(2, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Cold Breeze";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Arrays.asList("Gives +" + ChatColor.GREEN + coldbreeze.toPlainString() + ChatColor.RED + " ❁ Strength" + ChatColor.GRAY + " and", ChatColor.BLUE + "☠ Crit Damage" + ChatColor.GRAY + " when near snow");
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.EPIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Ice Shield";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    return Arrays.asList("Gain " + ChatColor.GREEN + iceshield.toPlainString() + "%" + ChatColor.GRAY + " of your strength", ChatColor.GRAY + "as " + ChatColor.GREEN + "❈ Defense");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Yeti Fury" + ChatColor.RED + ChatColor.BOLD + " COMING SOON!";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    return Arrays.asList("Buffs the Yeti sword  by " + ChatColor.GREEN + yeti1.toPlainString() + ChatColor.RED + " ❁", ChatColor.RED + "Damage" + ChatColor.GRAY + " and" + ChatColor.AQUA + " ✎", ChatColor.AQUA + "Intelligence");
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
        return "ab126814fc3fa846dad934c349628a7a1de5b415021a03ef4211d62514d5";
    }

    @Override
    public String getDisplayName() {
        return "Baby Yeti";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerStrength() {
        return 0.4;
    }

    @Override
    public double getPerIntelligence() {
        return 0.75;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public void particleBelowA(Player p, Location l) {
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 1.0f, 1.0f, 1.0f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.1882353f, 0.5411765f, 0.67058825f, 1.0f, 0, 64);
    }
}
