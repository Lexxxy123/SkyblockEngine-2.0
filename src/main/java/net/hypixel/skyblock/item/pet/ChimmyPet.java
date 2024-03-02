package net.hypixel.skyblock.item.pet;

import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChimmyPet extends Pet implements Untradeable {
    @Override
    public List<PetAbility> getPetAbilities(final SItem instance) {
        final int level = getLevel(instance);
        final RarityValue<Double> annihCh = new RarityValue<Double>(10.0, 10.0, 10.0, 8.0, 6.0, 6.0);
        final RarityValue<Integer> gingaCh = new RarityValue<Integer>(50, 40, 30, 20, 10, 10);
        final BigDecimal annih = BigDecimal.valueOf(1.0 / (annihCh.getForRarity(instance.getRarity()) - level * 0.02)).setScale(1, RoundingMode.HALF_EVEN);
        final BigDecimal pig = BigDecimal.valueOf(1.0 / (150.0 - level)).setScale(3, RoundingMode.HALF_EVEN);
        final List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Eww simp";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Collections.singletonList(ChatColor.GRAY + "yes this is a pet");
            }
        }));
        return abilities;
    }

    @Override
    public Skill getSkill() {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL() {
        return "99033a9e8eaf2529127eb2455d9a073244d9b65b2854e41ff26c4d73e9a7eaa5";
    }

    @Override
    public String getDisplayName() {
        return "Mini-Chimmy";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerIntelligence() {
        return 1.0;
    }

    @Override
    public double getPerMagicFind() {
        return 0.0;
    }

    @Override
    public double getPerDefense() {
        return 1.0;
    }

    @Override
    public double getPerCritDamage() {
        return 0.0;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.92156863f, 0.8980392f, 0.20392157f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.92156863f, 0.8980392f, 0.20392157f, 1.0f, 0, 64);
    }
}
