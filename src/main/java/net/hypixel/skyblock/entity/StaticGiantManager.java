/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity;

import net.hypixel.skyblock.entity.SEntity;

public final class StaticGiantManager {
    public static boolean ACTIVE = false;
    public static SEntity GIANT = null;

    public static void endFight() {
        if (GIANT == null) {
            return;
        }
        ACTIVE = false;
        GIANT = null;
    }
}

