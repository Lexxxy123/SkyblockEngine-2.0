/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.nms.nmsutil.apihelper;

import org.bukkit.plugin.Plugin;

public interface API {
    public void load();

    public void init(Plugin var1);

    public void disable(Plugin var1);
}

