/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.requirement.enums;

import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.EnchantingSkill;
import net.hypixel.skyblock.features.skill.FarmingSkill;
import net.hypixel.skyblock.features.skill.ForagingSkill;
import net.hypixel.skyblock.features.skill.MiningSkill;
import net.hypixel.skyblock.features.skill.Skill;

public enum SkillType {
    COMBAT(new CombatSkill()),
    FARMING(new FarmingSkill()),
    ENCHANTING(new EnchantingSkill()),
    FORAGING(new ForagingSkill()),
    MINING(new MiningSkill());

    private final Skill skill;

    private SkillType(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return this.skill;
    }
}

