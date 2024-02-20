package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.skill.CombatSkill;
import in.godspunky.skyblock.skill.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.RarityValue;
import in.godspunky.skyblock.item.SItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackCat extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(final SItem instance) {
        final RarityValue<Double> enderianMul = new RarityValue<Double>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        final RarityValue<Double> savvyMul = new RarityValue<Double>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        final int level = getLevel(instance);
        final int speed = level * 10 / 10;
        final double petlucc = level * 0.15 * 10.0 / 10.0;
        final double magicfind = level * 0.15 * 10.0 / 10.0;
        final List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Hunter";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Collections.singletonList("Increase your " + ChatColor.WHITE + "✦ Speed " + ChatColor.GRAY + "by " + ChatColor.GREEN + speed);
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Omen " + ChatColor.RED + ChatColor.BOLD + "COMING SOON!";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Collections.singletonList("Grants " + ChatColor.LIGHT_PURPLE + "+" + petlucc + " ♣ Pet Luck");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Supernatural";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Collections.singletonList("Grants " + ChatColor.AQUA + "+" + magicfind + " ✯ Magic Find");
                }
            });
        }
        return abilities;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public Skill getSkill() {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL() {
        return "e4b45cbaa19fe3d68c856cd3846c03b5f59de81a480eec921ab4fa3cd81317";
    }

    @Override
    public String getDisplayName() {
        return "Black Cat";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerSpeed() {
        return 0.0025;
    }

    @Override
    public double getPerIntelligence() {
        return 1.0;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.003921569f, 0.003921569f, 0.003921569f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.003921569f, 0.003921569f, 0.003921569f, 1.0f, 0, 64);
    }
}
