package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.skill.CombatSkill;
import in.godspunky.skyblock.skill.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AragornPet extends Pet implements Untradeable {
    @Override
    public List<PetAbility> getPetAbilities(final SItem instance) {
        final int level = Pet.getLevel(instance);
        final RarityValue<Double> annihCh = new RarityValue<Double>(10.0, 10.0, 10.0, 8.0, 6.0, 6.0);
        final RarityValue<Integer> gingaCh = new RarityValue<Integer>(50, 40, 30, 20, 10, 10);
        final BigDecimal annih = BigDecimal.valueOf(1.0 / (annihCh.getForRarity(instance.getRarity()) - level * 0.02)).setScale(1, RoundingMode.HALF_EVEN);
        final BigDecimal pig = BigDecimal.valueOf(1.0 / (150.0 - level)).setScale(3, RoundingMode.HALF_EVEN);
        final List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Skysim ownerrr";
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
        return "46097f84a73f099c0b1e517fd385c60785c3c7dbdf5667fbb58575ad54a256a6";
    }

    @Override
    public String getDisplayName() {
        return "Mini-Teriev";
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
        return -100.0;
    }

    @Override
    public double getPerMagicFind() {
        return 0.002;
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
    public double getPerCritDamage() {
        return 0.0;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }
}
