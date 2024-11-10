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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.item.pet.PetAbility;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BabyGhast
extends Pet {
    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        ArrayList<PetAbility> abilities = new ArrayList<PetAbility>();
        abilities.add(new PetAbility(){

            @Override
            public String getName() {
                return "Magic Find";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Collections.singletonList("Increases magic find by " + ChatColor.GREEN + "25%" + ChatColor.GRAY + " in the Nether.");
            }
        });
        abilities.add(new PetAbility(){

            @Override
            public String getName() {
                return "Lava Immunity";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Collections.singletonList("You are immune to lava damage.");
            }
        });
        abilities.add(new PetAbility(){

            @Override
            public String getName() {
                return "Ghast Immunity";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Collections.singletonList("Ghasts will not attack you.");
            }
        });
        abilities.add(new PetAbility(){

            @Override
            public String getName() {
                return "Temporary Flight";
            }

            @Override
            public List<String> getDescription(SItem instance) {
                return Arrays.asList("Fly for 10 seconds.", "Cooldown: 2 minutes.");
            }
        });
        return abilities;
    }

    @Override
    public Skill getSkill() {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL() {
        return "8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0";
    }

    @Override
    public String getDisplayName() {
        return "Baby Ghast";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerCritDamage() {
        return 699.0;
    }

    @Override
    public double getPerStrength() {
        return 699.0;
    }

    @Override
    public double getPerCritChance() {
        return 69.0;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public void particleBelowA(Player p, Location l) {
        p.spigot().playEffect(l, Effect.LARGE_SMOKE, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
        p.spigot().playEffect(l, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }
}

