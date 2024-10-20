/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.MaterialStatistics;

public interface ExperienceRewardStatistics
extends MaterialStatistics {
    public double getRewardXP();

    public Skill getRewardedSkill();
}

