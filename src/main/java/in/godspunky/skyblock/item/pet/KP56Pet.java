package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.features.skill.CombatSkill;
import in.godspunky.skyblock.features.skill.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KP56Pet extends Pet implements Untradeable {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        int level = Pet.getLevel(instance);
        RarityValue<Double> annihCh = new RarityValue<Double>(10.0, 10.0, 10.0, 8.0, 6.0, 6.0);
        RarityValue<Integer> gingaCh = new RarityValue<Integer>(50, 40, 30, 20, 10, 10);
        BigDecimal annih = BigDecimal.valueOf(1.0 / (annihCh.getForRarity(instance.getRarity()) - level * 0.02)).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal pig = BigDecimal.valueOf(1.0 / (150.0 - level)).setScale(3, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Big Brain";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Collections.singletonList(ChatColor.GRAY + "This guy is big brain");
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
        return "895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c";
    }

    @Override
    public String getDisplayName() {
        return "Mini-KP56";
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
        return 100.0;
    }

    @Override
    public double getPerMagicFind() {
        return 0.001;
    }

    @Override
    public double getPerStrength() {
        return 1.0;
    }

    @Override
    public double getPerDefense() {
        return 1.0;
    }

    @Override
    public double getPerCritChance() {
        return 0.002;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.SLIME, 0, 1, 0.92156863f, 0.8980392f, 0.20392157f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.SLIME, 0, 1, 0.92156863f, 0.8980392f, 0.20392157f, 1.0f, 0, 64);
    }
}
