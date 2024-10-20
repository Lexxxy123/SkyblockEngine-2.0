/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener;

public class Cancellable
implements org.bukkit.event.Cancellable {
    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b2) {
        this.cancelled = b2;
    }
}

