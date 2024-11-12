/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.dungeons.stats;

public class WrappedStats {
    private final float health;
    private final float healthRegen;
    private final float strength;
    private final float critDamage;
    private final float intelligence;
    private final float speed;
    private final float defense;
    private final float[] defaultArray;

    public WrappedStats(float[] z2) {
        this.health = z2[0];
        this.healthRegen = z2[1];
        this.strength = z2[2];
        this.critDamage = z2[3];
        this.intelligence = z2[4];
        this.speed = z2[5];
        this.defense = z2[6];
        this.defaultArray = z2;
    }

    public float getHealth() {
        return this.health;
    }

    public float getHealthRegen() {
        return this.healthRegen;
    }

    public float getStrength() {
        return this.strength;
    }

    public float getCritDamage() {
        return this.critDamage;
    }

    public float getIntelligence() {
        return this.intelligence;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getDefense() {
        return this.defense;
    }

    public float[] getDefaultArray() {
        return this.defaultArray;
    }
}

