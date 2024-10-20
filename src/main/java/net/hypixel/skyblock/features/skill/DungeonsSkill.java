/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.skill;

import java.util.List;

public interface DungeonsSkill {
    default public List<String> getPassive() {
        return null;
    }

    default public List<String> getGhost() {
        return null;
    }

    default public List<String> getOrb() {
        return null;
    }
}

