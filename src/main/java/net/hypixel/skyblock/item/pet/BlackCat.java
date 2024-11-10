/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.RarityValue;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.item.pet.PetAbility;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlackCat
extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        RarityValue<Double> enderianMul = new RarityValue<Double>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        RarityValue<Double> savvyMul = new RarityValue<Double>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        int level = BlackCat.getLevel(instance);
        final int speed = level * 10 / 10;
        final double petlucc = (double)level * 0.15 * 10.0 / 10.0;
        final double magicfind = (double)level * 0.15 * 10.0 / 10.0;
        ArrayList<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility(){

            @Override
            public String getName() {
                return "Hunter";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Collections.singletonList("Increase your " + ChatColor.WHITE + "\u2726 Speed " + ChatColor.GRAY + "by " + ChatColor.GREEN + speed);
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility(){

                @Override
                public String getName() {
                    return "Omen " + ChatColor.RED + ChatColor.BOLD + "COMING SOON!";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    return Collections.singletonList("Grants " + ChatColor.LIGHT_PURPLE + "+" + petlucc + " \u2663 Pet Luck");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility(){

                @Override
                public String getName() {
                    return "Supernatural";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    return Collections.singletonList("Grants " + ChatColor.AQUA + "+" + magicfind + " \u272f Magic Find");
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
    public void particleBelowA(Player p, Location l) {
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.003921569f, 0.003921569f, 0.003921569f, 1.0f, 0, 64);
        p.spigot().playEffect(l, Effect.COLOURED_DUST, 0, 1, 0.003921569f, 0.003921569f, 0.003921569f, 1.0f, 0, 64);
    }
}

