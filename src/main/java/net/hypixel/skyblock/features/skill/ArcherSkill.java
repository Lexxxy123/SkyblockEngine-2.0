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
        ArrayList<String> t = new ArrayList<String>();
        t.add("Doubleshot");
        t.add("Bone Plating");
        t.add("Bouncy Arrows" + ChatColor.RED + " Soon!");
        return t;
    }

    @Override
    public List<String> getOrb() {
        ArrayList<String> t = new ArrayList<String>();
        t.add("Explosive Shot");
        t.add("Machine Gun Bow");
        return t;
    }

    @Override
    public List<String> getGhost() {
        ArrayList<String> t = new ArrayList<String>();
        t.add("Stun Bow");
        t.add("Healing Bow");
        return t;
    }
}

