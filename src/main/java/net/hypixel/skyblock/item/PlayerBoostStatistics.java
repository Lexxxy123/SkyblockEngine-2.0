/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.MaterialStatistics;

public interface PlayerBoostStatistics
extends MaterialStatistics {
    default public int getBaseDamage() {
        return 0;
    }

    default public double getBaseStrength() {
        return 0.0;
    }

    default public double getBaseCritChance() {
        return 0.0;
    }

    default public double getBaseCritDamage() {
        return 0.0;
    }

    default public double getBaseMagicFind() {
        return 0.0;
    }

    default public double getBaseIntelligence() {
        return 0.0;
    }

    default public double getBaseSpeed() {
        return 0.0;
    }

    default public double getBaseHealth() {
        return 0.0;
    }

    default public double getBaseDefense() {
        return 0.0;
    }

    default public double getBaseAttackSpeed() {
        return 0.0;
    }

    default public double getBaseFerocity() {
        return 0.0;
    }
}

