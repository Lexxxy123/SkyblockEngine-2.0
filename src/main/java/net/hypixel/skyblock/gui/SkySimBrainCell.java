package net.hypixel.skyblock.gui;

import org.bukkit.plugin.Plugin;

public class SkySimBrainCell {
    public void accessAIFrom(BrainCellFor bcf) {
    }

    public static void applyAIToNMSPlayer(Object p, int limit, SkySimBrainCell sbc) {
    }

    public void getModules() {
    }

    public void startTraining(int level, Plugin pl, int limit, BrainCellFor bcf) {
    }

    public static SkySimBrainCell loadFromDB(String str) {
        return new SkySimBrainCell();
    }

    public enum BrainCellFor {
        MELEE,
        MOVEMENT,
        BOW_ATTACK,
        ENTITY_TRACKER,
        ABILITY_USAGE,
        ATTACK_PLAYER
    }
}
