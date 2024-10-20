/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;
import com.mongodb.annotations.Immutable;
import com.mongodb.lang.Nullable;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Immutable
public class ServerAddress
implements Serializable {
    private static final long serialVersionUID = 4027873363095395504L;
    private final String host;
    private final int port;

    public ServerAddress() {
        this(ServerAddress.defaultHost(), ServerAddress.defaultPort());
    }

    public ServerAddress(@Nullable String host) {
        this(host, ServerAddress.defaultPort());
    }

    public ServerAddress(InetAddress inetAddress) {
        this(inetAddress.getHostName(), ServerAddress.defaultPort());
    }

    public ServerAddress(InetAddress inetAddress, int port) {
        this(inetAddress.getHostName(), port);
    }

    public ServerAddress(InetSocketAddress inetSocketAddress) {
        this(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
    }

    public ServerAddress(@Nullable String host, int port) {
        String hostToUse = host;
        if (hostToUse == null) {
            hostToUse = ServerAddress.defaultHost();
        }
        if ((hostToUse = hostToUse.trim()).length() == 0) {
            hostToUse = ServerAddress.defaultHost();
        }
        int portToUse = port;
        if (hostToUse.startsWith("[")) {
            int idx = host.indexOf("]");
            if (idx == -1) {
                throw new IllegalArgumentException("an IPV6 address must be encosed with '[' and ']' according to RFC 2732.");
            }
            int portIdx = host.indexOf("]:");
            if (portIdx != -1) {
                if (port != ServerAddress.defaultPort()) {
                    throw new IllegalArgumentException("can't specify port in construct and via host");
                }
                portToUse = Integer.parseInt(host.substring(portIdx + 2));
            }
            hostToUse = host.substring(1, idx);
        } else {
            int lastIdx;
            int idx = hostToUse.indexOf(":");
            if (idx == (lastIdx = hostToUse.lastIndexOf(":")) && idx > 0) {
                if (port != ServerAddress.defaultPort()) {
                    throw new IllegalArgumentException("can't specify port in construct and via host");
                }
                try {
                    portToUse = Integer.parseInt(hostToUse.substring(idx + 1));
                } catch (NumberFormatException e2) {
                    throw new MongoException("host and port should be specified in host:port format");
                }
                hostToUse = hostToUse.substring(0, idx).trim();
            }
        }
        this.host = hostToUse.toLowerCase();
        this.port = portToUse;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ServerAddress that = (ServerAddress)o2;
        if (this.port != that.port) {
            return false;
        }
        return this.host.equals(that.host);
    }

    public int hashCode() {
        int result = this.host.hashCode();
        result = 31 * result + this.port;
        return result;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            return new InetSocketAddress(InetAddress.getByName(this.host), this.port);
        } catch (UnknownHostException e2) {
            throw new MongoSocketException(e2.getMessage(), this, (Throwable)e2);
        }
    }

    public List<InetSocketAddress> getSocketAddresses() {
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(this.host);
            ArrayList<InetSocketAddress> inetSocketAddressList = new ArrayList<InetSocketAddress>();
            for (InetAddress inetAddress : inetAddresses) {
                inetSocketAddressList.add(new InetSocketAddress(inetAddress, this.port));
            }
            return inetSocketAddressList;
        } catch (UnknownHostException e2) {
            throw new MongoSocketException(e2.getMessage(), this, (Throwable)e2);
        }
    }

    public String toString() {
        return this.host + ":" + this.port;
    }

    public static String defaultHost() {
        return "127.0.0.1";
    }

    public static int defaultPort() {
        return 27017;
    }

    @Deprecated
    public boolean sameHost(String hostName) {
        return this.equals(new ServerAddress(hostName));
    }
}

