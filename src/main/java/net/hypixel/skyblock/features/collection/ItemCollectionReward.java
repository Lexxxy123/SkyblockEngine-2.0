/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.collection;

import org.bukkit.entity.Player;

public abstract class ItemCollectionReward {
    private final Type type;

    protected ItemCollectionReward(Type type) {
        this.type = type;
    }

    public abstract void onAchieve(Player var1);

    public abstract String toRewardString();

    protected static enum Type {
        RECIPE,
        UPGRADE,
        EXPERIENCE;

    }
}

