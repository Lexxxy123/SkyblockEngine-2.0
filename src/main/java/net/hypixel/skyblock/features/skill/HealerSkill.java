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

public class HealerSkill
extends Skill
implements DungeonsSkill {
    public static final HealerSkill INSTANCE = new HealerSkill();

    @Override
    public String getName() {
        return "Healer";
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
        t.add("Renew");
        t.add("Healing Aura");
        t.add("Revive");
        t.add("Orbies" + ChatColor.RED + " Soon!");
        t.add("Soul Tether" + ChatColor.RED + " Soon!");
        return t;
    }

    @Override
    public List<String> getOrb() {
        ArrayList<String> t = new ArrayList<String>();
        t.add("Healing Circle");
        t.add("Wish");
        return t;
    }

    @Override
    public List<String> getGhost() {
        ArrayList<String> t = new ArrayList<String>();
        t.add("Healing Potion");
        t.add("Revive Self");
        return t;
    }
}

