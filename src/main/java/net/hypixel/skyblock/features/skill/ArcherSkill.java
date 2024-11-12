/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.features.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.skill.DungeonsSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;

public class ArcherSkill
extends Skill
implements DungeonsSkill {
    public static final ArcherSkill INSTANCE = new ArcherSkill();

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
        return Collections.singletonList("");
    }

    @Override
    public List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld) {
        return Collections.singletonList("");
    }

    @Override
    public boolean hasSixtyLevels() {
        return false;
    }

    @Override
    public void onSkillUpdate(User user, double previousXP) {
        super.onSkillUpdate(user, previousXP);
    }

    @Override
    public List<String> getPassive() {
        ArrayList<String> t2 = new ArrayList<String>();
        t2.add("Doubleshot");
        t2.add("Bone Plating");
        t2.add("Bouncy Arrows" + ChatColor.RED + " Soon!");
        return t2;
    }

    @Override
    public List<String> getOrb() {
        ArrayList<String> t2 = new ArrayList<String>();
        t2.add("Explosive Shot");
        t2.add("Machine Gun Bow");
        return t2;
    }

    @Override
    public List<String> getGhost() {
        ArrayList<String> t2 = new ArrayList<String>();
        t2.add("Stun Bow");
        t2.add("Healing Bow");
        return t2;
    }
}

