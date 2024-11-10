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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.RarityValue;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.Untradeable;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.item.pet.PetAbility;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AragornPet
extends Pet
implements Untradeable {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        int level = Pet.getLevel(instance);
        RarityValue<Double> annihCh = new RarityValue<Double>(10.0, 10.0, 10.0, 8.0, 6.0, 6.0);
        RarityValue<Integer> gingaCh = new RarityValue<Integer>(50, 40, 30, 20, 10, 10);
        BigDecimal annih = BigDecimal.valueOf(1.0 / (annihCh.getForRarity(instance.getRarity()) - (double)level * 0.02)).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal pig = BigDecimal.valueOf(1.0 / (150.0 - (double)level)).setScale(3, RoundingMode.HALF_EVEN);
        ArrayList<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility(){

            @Override
            public String getName() {
                return "Skysim ownerrr";
            }

            @Override
            public List<String> getDescription(SItem instance) {
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
    public void particleBelowA(Player p, Location l) {
        p.spigot().playEffect(l, Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }
}

