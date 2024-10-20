/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.orb;

public interface OrbBuff {
    public String getBuffName();

    public String getBuffDescription();

    default public String getCustomOrbName() {
        return null;
    }
}

