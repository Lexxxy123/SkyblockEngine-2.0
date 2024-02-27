package in.godspunky.skyblock.item.farming;

import in.godspunky.skyblock.features.skill.FarmingSkill;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.item.ExperienceRewardStatistics;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;

public class Pumpkin implements ExperienceRewardStatistics, MaterialFunction {
    @Override
    public double getRewardXP() {
        return 4.5;
    }

    @Override
    public Skill getRewardedSkill() {
        return FarmingSkill.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "Melon";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }
}
