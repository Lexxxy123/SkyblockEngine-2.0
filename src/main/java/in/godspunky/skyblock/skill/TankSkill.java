package in.godspunky.skyblock.skill;

import in.godspunky.skyblock.user.User;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TankSkill extends Skill implements DungeonsSkill {
    public static final TankSkill INSTANCE;

    static {
        INSTANCE = new TankSkill();
    }

    @Override
    public String getName() {
        return "Tank";
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
        t.add("Protective Barrier" + ChatColor.RED + " Soon!");
        t.add("Taunt");
        t.add("Diversion");
        t.add("Defensive Stance");
        return t;
    }

    @Override
    public List<String> getOrb() {
        final List<String> t = new ArrayList<String>();
        t.add("Seismic Wave");
        t.add("Castle of Stone");
        return t;
    }

    @Override
    public List<String> getGhost() {
        final List<String> t = new ArrayList<String>();
        t.add("Stun Potion");
        t.add("Absorption Potion");
        return t;
    }
}
