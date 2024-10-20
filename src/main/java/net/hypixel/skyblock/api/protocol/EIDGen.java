/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.protocol;

public class EIDGen {
    private static int lastIssuedEID = 2000000000;

    public static int generateEID() {
        int i2 = lastIssuedEID++;
        return i2;
    }
}

