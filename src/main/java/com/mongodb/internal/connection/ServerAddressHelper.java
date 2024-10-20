/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.UnixServerAddress;

public final class ServerAddressHelper {
    public static ServerAddress createServerAddress(String host) {
        return ServerAddressHelper.createServerAddress(host, ServerAddress.defaultPort());
    }

    public static ServerAddress createServerAddress(String host, int port) {
        if (host != null && host.endsWith(".sock")) {
            return new UnixServerAddress(host);
        }
        return new ServerAddress(host, port);
    }

    private ServerAddressHelper() {
    }
}

