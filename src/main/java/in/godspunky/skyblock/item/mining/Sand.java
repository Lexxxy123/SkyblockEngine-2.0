package in.godspunky.skyblock.item.mining;

import in.godspunky.skyblock.features.skill.MiningSkill;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.item.ExperienceRewardStatistics;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;

public class Sand implements ExperienceRewardStatistics, MaterialFunction {
    @Override
    public double getRewardXP() {
        return 3.0;
    }

    @Override
    public Skill getRewardedSkill() {
        return MiningSkill.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "Sand";
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
