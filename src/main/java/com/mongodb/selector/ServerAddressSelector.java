/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.selector;

import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.selector.ServerSelector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Deprecated
public class ServerAddressSelector
implements ServerSelector {
    private final ServerAddress serverAddress;

    public ServerAddressSelector(ServerAddress serverAddress) {
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
    }

    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }

    @Override
    public List<ServerDescription> select(ClusterDescription clusterDescription) {
        if (clusterDescription.getByServerAddress(this.serverAddress) != null) {
            return Arrays.asList(clusterDescription.getByServerAddress(this.serverAddress));
        }
        return Collections.emptyList();
    }

    public String toString() {
        return "ServerAddressSelector{serverAddress=" + this.serverAddress + '}';
    }
}

