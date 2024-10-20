/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener;

import org.bukkit.event.Cancellable;

public interface IPacketListener {
    public Object onPacketSend(Object var1, Object var2, Cancellable var3);

    public Object onPacketReceive(Object var1, Object var2, Cancellable var3);
}

