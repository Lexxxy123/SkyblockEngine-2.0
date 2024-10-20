/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  jnr.unixsocket.UnixSocketAddress
 */
package com.mongodb;

import com.mongodb.ServerAddress;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import jnr.unixsocket.UnixSocketAddress;

@Immutable
public final class UnixServerAddress
extends ServerAddress {
    private static final long serialVersionUID = 154466643544866543L;

    public UnixServerAddress(String path) {
        super(Assertions.notNull("The path cannot be null", path));
        Assertions.isTrueArgument("The path must end in .sock", path.endsWith(".sock"));
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        throw new UnsupportedOperationException("Cannot return a InetSocketAddress from a UnixServerAddress");
    }

    public SocketAddress getUnixSocketAddress() {
        return new UnixSocketAddress(this.getHost());
    }

    @Override
    public String toString() {
        return this.getHost();
    }
}

