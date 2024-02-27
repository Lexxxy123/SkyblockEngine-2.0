package in.godspunky.skyblock.item;

import in.godspunky.skyblock.features.skill.Skill;

public interface ExperienceRewardStatistics extends MaterialStatistics {
    double getRewardXP();

    Skill getRewardedSkill();
}
