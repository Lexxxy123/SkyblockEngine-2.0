/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package net.hypixel.skyblock.item.pet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.RarityValue;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.item.pet.PetAbility;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class VoidlingsPet
extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        RarityValue<Double> enderianMul = new RarityValue<Double>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        RarityValue<Double> savvyMul = new RarityValue<Double>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        final int level = VoidlingsPet.getLevel(instance);
        BigDecimal enderian = new BigDecimal((double)level * enderianMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal savvy = new BigDecimal((double)level * savvyMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal zealot = new BigDecimal((double)level * 0.25).setScale(2, RoundingMode.HALF_EVEN);
        ArrayList<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility(){

            @Override
            public String getName() {
                return "Voidling Killer";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Arrays.asList("Helps you to defeat " + ChatColor.DARK_PURPLE + "Voidlings", ChatColor.DARK_PURPLE + "Warden " + ChatColor.GRAY + "And Boost Your " + ChatColor.LIGHT_PURPLE + "Stats", Sputnik.trans("&4\u2620 &cRequires &5Enderman Slayer 6."));
            }

            @Override
            public void onHurt(EntityDamageByEntityEvent e, Entity damager) {
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.RARE)) {
            abilities.add(new PetAbility(){

                @Override
                public String getName() {
                    return "Ender's Stronghold";
                }

                @Override
                public List<String> getDescription(SItem instance) {
                    BigDecimal e = new BigDecimal(0.9 * (double)level).setScale(1, RoundingMode.HALF_EVEN);
                    return Collections.singletonList(Sputnik.trans("&7Take &a" + e.toPlainString() + "% &7less damage from ender mobs."));
                }

                @Override
                public void onHurt(EntityDamageByEntityEvent e, Entity damager) {
                    SEntity entity = SEntity.findSEntity(damager);
                    if (null == entity) {
                        return;
                    }
                    if (Groups.END_MOBS.contains((Object)entity.getSpecType())) {
                        e.setDamage(e.getDamage() - e.getDamage() * (0.3 * (double)level) / 100.0);
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
        return "9a519f6fcb59d40c7f88809295b139fb300295ad450ef6e8a4e0f0acca0dfbdd";
    }

    @Override
    public String getDisplayName() {
        return "Voidling Destroyer";
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
        return 1.1;
    }

    @Override
    public double getPerStrength() {
        return 500.0;
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

