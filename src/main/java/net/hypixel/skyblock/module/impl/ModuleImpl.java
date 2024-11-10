/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.module.impl;

public interface ModuleImpl {
    public String name();

    public void onStart();

    public void onStop();

    public boolean shouldStart();
}

