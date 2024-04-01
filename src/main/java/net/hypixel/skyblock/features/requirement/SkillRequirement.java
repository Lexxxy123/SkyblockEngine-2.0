package net.hypixel.skyblock.features.requirement;

import net.hypixel.skyblock.features.requirement.enums.SkillType;
import net.hypixel.skyblock.user.User;

public class SkillRequirement implements AbstractRequirement {

    private final SkillType skillType;
    private final int level;

    public SkillRequirement(SkillType skillType , int level){
        this.skillType = skillType;
        this.level = level;
    }


    @Override
    public boolean hasRequirement(User user) {
        return user.getSkillLevel(skillType) >= level;
    }

    @Override
    public String getMessage() {
        return "You need " + skillType.getSkill().getName() + " level " + level + " to perform this action";
    }
}
