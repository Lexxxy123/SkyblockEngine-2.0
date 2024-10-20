/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.collection;

public enum ItemCollectionCategory {
    FARMING,
    MINING,
    COMBAT,
    FORAGING,
    FISHING,
    BOSS;


    public String getName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}

