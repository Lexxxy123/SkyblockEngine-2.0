/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DefaultDBDecoder;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;

@Deprecated
public class MongoOptions {
    public String description;
    public int connectionsPerHost;
    public int threadsAllowedToBlockForConnectionMultiplier;
    public int maxWaitTime;
    public int connectTimeout;
    public int socketTimeout;
    public boolean socketKeepAlive;
    public ReadPreference readPreference;
    public DBDecoderFactory dbDecoderFactory;
    public DBEncoderFactory dbEncoderFactory;
    public boolean safe;
    public int w;
    public int wtimeout;
    public boolean fsync;
    public boolean j;
    public SocketFactory socketFactory;
    public boolean cursorFinalizerEnabled;
    public WriteConcern writeConcern;
    public boolean alwaysUseMBeans;
    String requiredReplicaSetName;

    @Deprecated
    public MongoOptions() {
        this.reset();
    }

    @Deprecated
    public MongoOptions(MongoClientOptions options) {
        this.connectionsPerHost = options.getConnectionsPerHost();
        this.threadsAllowedToBlockForConnectionMultiplier = options.getThreadsAllowedToBlockForConnectionMultiplier();
        this.maxWaitTime = options.getMaxWaitTime();
        this.connectTimeout = options.getConnectTimeout();
        this.socketFactory = options.getSocketFactory();
        this.socketTimeout = options.getSocketTimeout();
        this.socketKeepAlive = options.isSocketKeepAlive();
        this.readPreference = options.getReadPreference();
        this.dbDecoderFactory = options.getDbDecoderFactory();
        this.dbEncoderFactory = options.getDbEncoderFactory();
        this.description = options.getDescription();
        this.writeConcern = options.getWriteConcern();
        this.alwaysUseMBeans = options.isAlwaysUseMBeans();
        this.requiredReplicaSetName = options.getRequiredReplicaSetName();
    }

    public void reset() {
        this.connectionsPerHost = 10;
        this.threadsAllowedToBlockForConnectionMultiplier = 5;
        this.maxWaitTime = 120000;
        this.connectTimeout = 10000;
        this.socketFactory = SocketFactory.getDefault();
        this.socketTimeout = 0;
        this.socketKeepAlive = false;
        this.readPreference = null;
        this.writeConcern = null;
        this.safe = false;
        this.w = 0;
        this.wtimeout = 0;
        this.fsync = false;
        this.j = false;
        this.dbDecoderFactory = DefaultDBDecoder.FACTORY;
        this.dbEncoderFactory = DefaultDBEncoder.FACTORY;
        this.description = null;
        this.cursorFinalizerEnabled = true;
        this.alwaysUseMBeans = false;
        this.requiredReplicaSetName = null;
    }

    public MongoOptions copy() {
        MongoOptions m2 = new MongoOptions();
        m2.connectionsPerHost = this.connectionsPerHost;
        m2.threadsAllowedToBlockForConnectionMultiplier = this.threadsAllowedToBlockForConnectionMultiplier;
        m2.maxWaitTime = this.maxWaitTime;
        m2.connectTimeout = this.connectTimeout;
        m2.socketFactory = this.socketFactory;
        m2.socketTimeout = this.socketTimeout;
        m2.socketKeepAlive = this.socketKeepAlive;
        m2.readPreference = this.readPreference;
        m2.writeConcern = this.writeConcern;
        m2.safe = this.safe;
        m2.w = this.w;
        m2.wtimeout = this.wtimeout;
        m2.fsync = this.fsync;
        m2.j = this.j;
        m2.dbDecoderFactory = this.dbDecoderFactory;
        m2.dbEncoderFactory = this.dbEncoderFactory;
        m2.description = this.description;
        m2.cursorFinalizerEnabled = this.cursorFinalizerEnabled;
        m2.alwaysUseMBeans = this.alwaysUseMBeans;
        m2.requiredReplicaSetName = this.requiredReplicaSetName;
        return m2;
    }

    MongoClientOptions toClientOptions() {
        MongoClientOptions.Builder builder = MongoClientOptions.builder().requiredReplicaSetName(this.requiredReplicaSetName).connectionsPerHost(this.connectionsPerHost).connectTimeout(this.connectTimeout).dbDecoderFactory(this.dbDecoderFactory).dbEncoderFactory(this.dbEncoderFactory).description(this.description).maxWaitTime(this.maxWaitTime).socketFactory(this.socketFactory).socketKeepAlive(this.socketKeepAlive).socketTimeout(this.socketTimeout).threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier).cursorFinalizerEnabled(this.cursorFinalizerEnabled).alwaysUseMBeans(this.alwaysUseMBeans);
        builder.writeConcern(this.getWriteConcern());
        if (this.readPreference != null) {
            builder.readPreference(this.getReadPreference());
        }
        return builder.build();
    }

    public WriteConcern getWriteConcern() {
        WriteConcern retVal;
        if (this.writeConcern != null) {
            retVal = this.writeConcern;
        } else if (this.w != 0 || this.wtimeout != 0 || this.fsync | this.j) {
            retVal = WriteConcern.ACKNOWLEDGED;
            if (this.w != 0) {
                retVal = retVal.withW(this.w);
            }
            if (this.wtimeout != 0) {
                retVal = retVal.withWTimeout(this.wtimeout, TimeUnit.MILLISECONDS);
            }
            if (this.fsync) {
                retVal = retVal.withFsync(this.fsync);
            }
            if (this.j) {
                retVal = retVal.withJ(this.j);
            }
        } else {
            retVal = this.safe ? WriteConcern.ACKNOWLEDGED : WriteConcern.UNACKNOWLEDGED;
        }
        return retVal;
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    public synchronized SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public synchronized void setSocketFactory(SocketFactory factory) {
        this.socketFactory = factory;
    }

    public synchronized String getDescription() {
        return this.description;
    }

    public synchronized void setDescription(String desc) {
        this.description = desc;
    }

    public synchronized int getConnectionsPerHost() {
        return this.connectionsPerHost;
    }

    public synchronized void setConnectionsPerHost(int connections) {
        this.connectionsPerHost = connections;
    }

    public synchronized int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    public synchronized void setThreadsAllowedToBlockForConnectionMultiplier(int threads) {
        this.threadsAllowedToBlockForConnectionMultiplier = threads;
    }

    public synchronized int getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public synchronized void setMaxWaitTime(int timeMS) {
        this.maxWaitTime = timeMS;
    }

    public synchronized int getConnectTimeout() {
        return this.connectTimeout;
    }

    public synchronized void setConnectTimeout(int timeoutMS) {
        this.connectTimeout = timeoutMS;
    }

    public synchronized int getSocketTimeout() {
        return this.socketTimeout;
    }

    public synchronized void setSocketTimeout(int timeoutMS) {
        this.socketTimeout = timeoutMS;
    }

    public synchronized boolean isSocketKeepAlive() {
        return this.socketKeepAlive;
    }

    public synchronized void setSocketKeepAlive(boolean keepAlive) {
        this.socketKeepAlive = keepAlive;
    }

    public synchronized DBDecoderFactory getDbDecoderFactory() {
        return this.dbDecoderFactory;
    }

    public synchronized void setDbDecoderFactory(DBDecoderFactory factory) {
        this.dbDecoderFactory = factory;
    }

    public synchronized DBEncoderFactory getDbEncoderFactory() {
        return this.dbEncoderFactory;
    }

    public synchronized void setDbEncoderFactory(DBEncoderFactory factory) {
        this.dbEncoderFactory = factory;
    }

    public synchronized boolean isSafe() {
        return this.safe;
    }

    public synchronized void setSafe(boolean isSafe) {
        this.safe = isSafe;
    }

    public synchronized int getW() {
        return this.w;
    }

    public synchronized void setW(int val) {
        this.w = val;
    }

    public synchronized int getWtimeout() {
        return this.wtimeout;
    }

    public synchronized void setWtimeout(int timeoutMS) {
        this.wtimeout = timeoutMS;
    }

    public synchronized boolean isFsync() {
        return this.fsync;
    }

    public synchronized void setFsync(boolean sync) {
        this.fsync = sync;
    }

    public synchronized boolean isJ() {
        return this.j;
    }

    public synchronized void setJ(boolean safe) {
        this.j = safe;
    }

    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public boolean isCursorFinalizerEnabled() {
        return this.cursorFinalizerEnabled;
    }

    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    public boolean isAlwaysUseMBeans() {
        return this.alwaysUseMBeans;
    }

    public void setAlwaysUseMBeans(boolean alwaysUseMBeans) {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }

    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoOptions options = (MongoOptions)o2;
        if (this.alwaysUseMBeans != options.alwaysUseMBeans) {
            return false;
        }
        if (this.connectTimeout != options.connectTimeout) {
            return false;
        }
        if (this.connectionsPerHost != options.connectionsPerHost) {
            return false;
        }
        if (this.cursorFinalizerEnabled != options.cursorFinalizerEnabled) {
            return false;
        }
        if (this.fsync != options.fsync) {
            return false;
        }
        if (this.j != options.j) {
            return false;
        }
        if (this.maxWaitTime != options.maxWaitTime) {
            return false;
        }
        if (this.safe != options.safe) {
            return false;
        }
        if (this.socketKeepAlive != options.socketKeepAlive) {
            return false;
        }
        if (this.socketTimeout != options.socketTimeout) {
            return false;
        }
        if (this.threadsAllowedToBlockForConnectionMultiplier != options.threadsAllowedToBlockForConnectionMultiplier) {
            return false;
        }
        if (this.w != options.w) {
            return false;
        }
        if (this.wtimeout != options.wtimeout) {
            return false;
        }
        if (this.dbDecoderFactory != null ? !this.dbDecoderFactory.equals(options.dbDecoderFactory) : options.dbDecoderFactory != null) {
            return false;
        }
        if (this.dbEncoderFactory != null ? !this.dbEncoderFactory.equals(options.dbEncoderFactory) : options.dbEncoderFactory != null) {
            return false;
        }
        if (this.description != null ? !this.description.equals(options.description) : options.description != null) {
            return false;
        }
        if (this.readPreference != null ? !this.readPreference.equals(options.readPreference) : options.readPreference != null) {
            return false;
        }
        if (this.socketFactory != null ? !this.socketFactory.equals(options.socketFactory) : options.socketFactory != null) {
            return false;
        }
        if (this.writeConcern != null ? !this.writeConcern.equals(options.writeConcern) : options.writeConcern != null) {
            return false;
        }
        return !(this.requiredReplicaSetName != null ? !this.requiredReplicaSetName.equals(options.requiredReplicaSetName) : options.requiredReplicaSetName != null);
    }

    public int hashCode() {
        int result = this.description != null ? this.description.hashCode() : 0;
        result = 31 * result + this.connectionsPerHost;
        result = 31 * result + this.threadsAllowedToBlockForConnectionMultiplier;
        result = 31 * result + this.maxWaitTime;
        result = 31 * result + this.connectTimeout;
        result = 31 * result + this.socketTimeout;
        result = 31 * result + (this.socketKeepAlive ? 1 : 0);
        result = 31 * result + (this.readPreference != null ? this.readPreference.hashCode() : 0);
        result = 31 * result + (this.dbDecoderFactory != null ? this.dbDecoderFactory.hashCode() : 0);
        result = 31 * result + (this.dbEncoderFactory != null ? this.dbEncoderFactory.hashCode() : 0);
        result = 31 * result + (this.safe ? 1 : 0);
        result = 31 * result + this.w;
        result = 31 * result + this.wtimeout;
        result = 31 * result + (this.fsync ? 1 : 0);
        result = 31 * result + (this.j ? 1 : 0);
        result = 31 * result + (this.socketFactory != null ? this.socketFactory.hashCode() : 0);
        result = 31 * result + (this.cursorFinalizerEnabled ? 1 : 0);
        result = 31 * result + (this.writeConcern != null ? this.writeConcern.hashCode() : 0);
        result = 31 * result + (this.alwaysUseMBeans ? 1 : 0);
        result = 31 * result + (this.requiredReplicaSetName != null ? this.requiredReplicaSetName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "MongoOptions{description='" + this.description + '\'' + ", connectionsPerHost=" + this.connectionsPerHost + ", threadsAllowedToBlockForConnectionMultiplier=" + this.threadsAllowedToBlockForConnectionMultiplier + ", maxWaitTime=" + this.maxWaitTime + ", connectTimeout=" + this.connectTimeout + ", socketTimeout=" + this.socketTimeout + ", socketKeepAlive=" + this.socketKeepAlive + ", readPreference=" + this.readPreference + ", dbDecoderFactory=" + this.dbDecoderFactory + ", dbEncoderFactory=" + this.dbEncoderFactory + ", safe=" + this.safe + ", w=" + this.w + ", wtimeout=" + this.wtimeout + ", fsync=" + this.fsync + ", j=" + this.j + ", socketFactory=" + this.socketFactory + ", cursorFinalizerEnabled=" + this.cursorFinalizerEnabled + ", writeConcern=" + this.writeConcern + ", alwaysUseMBeans=" + this.alwaysUseMBeans + ", requiredReplicaSetName=" + this.requiredReplicaSetName + '}';
    }
}

