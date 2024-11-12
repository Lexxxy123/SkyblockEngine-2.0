/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.assertions.Assertions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerVersion
implements Comparable<ServerVersion> {
    private final List<Integer> versionList;

    public ServerVersion() {
        this.versionList = Collections.unmodifiableList(Arrays.asList(0, 0, 0));
    }

    public ServerVersion(List<Integer> versionList) {
        Assertions.notNull("versionList", versionList);
        Assertions.isTrue("version array has three elements", versionList.size() == 3);
        this.versionList = Collections.unmodifiableList(new ArrayList<Integer>(versionList));
    }

    public ServerVersion(int majorVersion, int minorVersion) {
        this(Arrays.asList(majorVersion, minorVersion, 0));
    }

    public List<Integer> getVersionList() {
        return this.versionList;
    }

    @Override
    public int compareTo(ServerVersion o2) {
        int retVal = 0;
        for (int i2 = 0; i2 < this.versionList.size() && (retVal = this.versionList.get(i2).compareTo(o2.versionList.get(i2))) == 0; ++i2) {
        }
        return retVal;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ServerVersion that = (ServerVersion)o2;
        return this.versionList.equals(that.versionList);
    }

    public int hashCode() {
        return this.versionList.hashCode();
    }

    public String toString() {
        return "ServerVersion{versionList=" + this.versionList + '}';
    }
}

