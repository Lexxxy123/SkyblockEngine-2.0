/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.gui;

import org.bukkit.plugin.Plugin;

public class SkySimBrainCell {
    public void accessAIFrom(BrainCellFor bcf) {
    }

    public static void applyAIToNMSPlayer(Object p2, int limit, SkySimBrainCell sbc) {
    }

    public void getModules() {
    }

    public void startTraining(int level, Plugin pl, int limit, BrainCellFor bcf) {
    }

    public static SkySimBrainCell loadFromDB(String str) {
        return new SkySimBrainCell();
    }

    public static enum BrainCellFor {
        MELEE,
        MOVEMENT,
        BOW_ATTACK,
        ENTITY_TRACKER,
        ABILITY_USAGE,
        ATTACK_PLAYER;

    }
}

