package vn.giakhanhvn.skysim.item;

import vn.giakhanhvn.skysim.skill.Skill;

public interface ExperienceRewardStatistics extends MaterialStatistics {
    double getRewardXP();

    Skill getRewardedSkill();
}
