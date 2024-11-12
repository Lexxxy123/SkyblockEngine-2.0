/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.net.InetAddress;

@Deprecated
public class DBAddress
extends ServerAddress {
    private static final long serialVersionUID = -813211264765778133L;
    private final String _db;

    public DBAddress(String urlFormat) {
        super(DBAddress._getHostSection(urlFormat));
        DBAddress._check(urlFormat, "urlFormat");
        this._db = DBAddress._fixName(DBAddress._getDBSection(urlFormat));
        DBAddress._check(this.getHost(), "host");
        DBAddress._check(this._db, "db");
    }

    @Nullable
    static String _getHostSection(String urlFormat) {
        Assertions.notNull("urlFormat", urlFormat);
        int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(0, idx);
        }
        return null;
    }

    static String _getDBSection(String urlFormat) {
        Assertions.notNull("urlFormat", urlFormat);
        int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(idx + 1);
        }
        return urlFormat;
    }

    static String _fixName(String name) {
        return name.replace('.', '-');
    }

    public DBAddress(DBAddress other, String databaseName) {
        this(other.getHost(), other.getPort(), databaseName);
    }

    public DBAddress(String host, String databaseName) {
        this(host, DBAddress.defaultPort(), databaseName);
    }

    public DBAddress(String host, int port, String databaseName) {
        super(host, port);
        this._db = databaseName.trim();
    }

    public DBAddress(InetAddress inetAddress, int port, String databaseName) {
        super(inetAddress, port);
        DBAddress._check(databaseName, "name");
        this._db = databaseName.trim();
    }

    static void _check(String thing, String name) {
        Assertions.notNull("thing", thing);
        String trimmedThing = thing.trim();
        if (trimmedThing.length() == 0) {
            throw new IllegalArgumentException(name + " can't be empty");
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this._db.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DBAddress) {
            DBAddress a2 = (DBAddress)other;
            return a2.getPort() == this.getPort() && a2._db.equals(this._db) && a2.getHost().equals(this.getHost());
        }
        if (other instanceof ServerAddress) {
            return other.equals(this);
        }
        return false;
    }

    public DBAddress getSister(String name) {
        return new DBAddress(this.getHost(), this.getPort(), name);
    }

    public String getDBName() {
        return this._db;
    }

    @Override
    public String toString() {
        return super.toString() + "/" + this._db;
    }
}

