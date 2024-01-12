package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.RarityValue;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.skill.CombatSkill;
import in.godspunky.skyblock.skill.Skill;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GungaPet extends Pet {
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
                return "Ig this is yours now";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Collections.singletonList(ChatColor.RED + "nulled lol");
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
        return "";
    }

    @Override
    public String getDisplayName() {
        return "null";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerIntelligence() {
        return 0.0;
    }

    @Override
    public double getPerMagicFind() {
        return 0.0;
    }

    @Override
    public double getPerDefense() {
        return 0.0;
    }

    @Override
    public double getPerCritDamage() {
        return 0.0;
    }
}
