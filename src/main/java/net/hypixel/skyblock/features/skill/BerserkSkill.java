package net.hypixel.skyblock.features.skill;

import net.hypixel.skyblock.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BerserkSkill extends Skill implements DungeonsSkill {
    public static final BerserkSkill INSTANCE;

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public String getAlternativeName() {
        return "{skip}";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("");
    }

    @Override
    public List<String> getLevelUpInformation(final int level, final int lastLevel, final boolean showOld) {
        return Collections.singletonList("");
    }

    @Override
    public boolean hasSixtyLevels() {
        return false;
    }

    @Override
    public void onSkillUpdate(final User user, final double previousXP) {
        super.onSkillUpdate(user, previousXP);
    }

    @Override
    public List<String> getPassive() {
        final List<String> t = new ArrayList<String>();
        t.add("Bloodlust");
        t.add("Lust for Blood");
        return t;
    }

    @Override
    public List<String> getOrb() {
        final List<String> t = new ArrayList<String>();
        t.add("Throwing Axe");
        t.add("Ragnagrok");
        return t;
    }

    @Override
    public List<String> getGhost() {
        final List<String> t = new ArrayList<String>();
        t.add("Throwing Axe");
        t.add("Strength Potion");
        return t;
    }

    static {
        INSTANCE = new BerserkSkill();
    }
}
