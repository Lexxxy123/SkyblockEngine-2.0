package in.godspunky.skyblock.item.foraging;

import in.godspunky.skyblock.item.ExperienceRewardStatistics;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.skill.ForagingSkill;
import in.godspunky.skyblock.skill.Skill;

public class SpruceWood implements ExperienceRewardStatistics, MaterialFunction {
    @Override
    public double getRewardXP() {
        return 6.0;
    }

    @Override
    public Skill getRewardedSkill() {
        return ForagingSkill.INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "Spruce Wood";
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
