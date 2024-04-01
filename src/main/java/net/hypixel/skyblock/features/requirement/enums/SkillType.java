package net.hypixel.skyblock.features.requirement.enums;

import lombok.Getter;
import net.hypixel.skyblock.features.skill.*;

public enum SkillType {
    COMBAT(new CombatSkill()),
    FARMING(new FarmingSkill()),
    ENCHANTING(new EnchantingSkill()),
    FORAGING(new ForagingSkill()),
    MINING(new MiningSkill());


    @Getter
    private final Skill skill;

    SkillType(Skill skill){
        this.skill = skill;
    }


}
