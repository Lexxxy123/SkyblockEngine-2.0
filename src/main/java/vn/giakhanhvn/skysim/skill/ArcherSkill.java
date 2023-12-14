package vn.giakhanhvn.skysim.skill;

import org.bukkit.ChatColor;
import java.util.ArrayList;
import vn.giakhanhvn.skysim.user.User;
import java.util.Arrays;
import java.util.List;

public class ArcherSkill extends Skill implements DungeonsSkill
{
    public static final ArcherSkill INSTANCE;
    
    @Override
    public String getName() {
        return "Archer";
    }
    
    @Override
    public String getAlternativeName() {
        return "{skip}";
    }
    
    @Override
    public List<String> getDescription() {
        return Arrays.<String>asList("");
    }
    
    @Override
    public List<String> getLevelUpInformation(final int level, final int lastLevel, final boolean showOld) {
        return Arrays.<String>asList("");
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
        t.add("Doubleshot");
        t.add("Bone Plating");
        t.add("Bouncy Arrows" + ChatColor.RED + " Soon!");
        return t;
    }
    
    @Override
    public List<String> getOrb() {
        final List<String> t = new ArrayList<String>();
        t.add("Explosive Shot");
        t.add("Machine Gun Bow");
        return t;
    }
    
    @Override
    public List<String> getGhost() {
        final List<String> t = new ArrayList<String>();
        t.add("Stun Bow");
        t.add("Healing Bow");
        return t;
    }
    
    static {
        INSTANCE = new ArcherSkill();
    }
}
