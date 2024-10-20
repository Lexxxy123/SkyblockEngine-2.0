/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.connection.ClusterType;

public enum ServerType {
    STANDALONE{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.STANDALONE;
        }
    }
    ,
    REPLICA_SET_PRIMARY{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.REPLICA_SET;
        }
    }
    ,
    REPLICA_SET_SECONDARY{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.REPLICA_SET;
        }
    }
    ,
    REPLICA_SET_ARBITER{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.REPLICA_SET;
        }
    }
    ,
    REPLICA_SET_OTHER{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.REPLICA_SET;
        }
    }
    ,
    REPLICA_SET_GHOST{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.REPLICA_SET;
        }
    }
    ,
    SHARD_ROUTER{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.SHARDED;
        }
    }
    ,
    UNKNOWN{

        @Override
        public ClusterType getClusterType() {
            return ClusterType.UNKNOWN;
        }
    };


    public abstract ClusterType getClusterType();
}

