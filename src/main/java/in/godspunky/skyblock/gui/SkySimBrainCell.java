package in.godspunky.skyblock.gui;

import org.bukkit.plugin.Plugin;

public class SkySimBrainCell {
    public static void applyAIToNMSPlayer(final Object p, final int limit, final SkySimBrainCell sbc) {
    }

    public static SkySimBrainCell loadFromDB(final String str) {
        return new SkySimBrainCell();
    }

    public void accessAIFrom(final BrainCellFor bcf) {
    }

    public void getModules() {
    }

    public void startTraining(final int level, final Plugin pl, final int limit, final BrainCellFor bcf) {
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
