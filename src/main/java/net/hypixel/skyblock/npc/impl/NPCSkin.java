/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.npc.impl;

public class NPCSkin {
    private final String texture;
    private final String signature;

    public String getTexture() {
        return this.texture;
    }

    public String getSignature() {
        return this.signature;
    }

    public NPCSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }
}

