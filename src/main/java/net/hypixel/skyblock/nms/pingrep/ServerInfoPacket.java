/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.pingrep;

import net.hypixel.skyblock.nms.pingrep.PingReply;

public abstract class ServerInfoPacket {
    private PingReply reply;

    public ServerInfoPacket(PingReply reply) {
        this.reply = reply;
    }

    public abstract void send();

    public PingReply getReply() {
        return this.reply;
    }

    public void setReply(PingReply reply) {
        this.reply = reply;
    }
}

