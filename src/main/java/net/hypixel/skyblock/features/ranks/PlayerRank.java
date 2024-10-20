/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.ranks;

public enum PlayerRank {
    DEFAULT("&7", 1),
    VIP("&a[VIP]", 2),
    VIPPLUS("&a[VIP&6+&a]", 3),
    MVP("&b[MVP]", 4),
    MVPPLUS("&b[MVP&c+&b]", 5),
    MVPPLUSPLUS("&6[MVP&c++&6]", 6),
    YOUTUBE("&c[&fYOUTUBE&c]", 7),
    BT("&d[BT]", 8),
    SPECIAL("&e[SPECIAL]", 9),
    JRHELPER("&9[JR HELPER]", 10),
    HELPER("&9[HELPER]", 11),
    MOD("&2[MOD]", 12),
    GAMEMASTER("&2[GM]", 13),
    BUILD("&3[BUILD TEAM]", 14),
    ADMIN("&c[ADMIN]", 15),
    MANAGER("&c[MANAGER]", 16),
    WATCHDOG("&c[WATCHDOG]", 17),
    JERRY("&d[JERRY++]", 18),
    OWNER("&c[OWNER]", 19);

    private final String prefix;
    private final int level;

    private PlayerRank(String prefix, int level) {
        this.prefix = prefix;
        this.level = level;
    }

    public static PlayerRank getRankOrDefault(int level) {
        for (PlayerRank rank : PlayerRank.values()) {
            if (rank.level != level) continue;
            return rank;
        }
        return DEFAULT;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isBelowOrEqual(PlayerRank rank) {
        return this.level <= rank.level;
    }

    public boolean isAboveOrEqual(PlayerRank rank) {
        return this.level >= rank.level;
    }

    public boolean hasRank(PlayerRank requiredRank) {
        return this.level >= requiredRank.level;
    }

    public boolean isStaff() {
        return this.level >= PlayerRank.HELPER.level;
    }

    public boolean isDefaultPermission() {
        return this == DEFAULT;
    }

    public String getFormattedRank() {
        return this.prefix;
    }
}

