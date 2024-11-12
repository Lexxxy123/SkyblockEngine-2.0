/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.assertions.Assertions;
import java.util.HashMap;
import java.util.Map;

public abstract class IndexMap {
    public static IndexMap create() {
        return new RangeBased();
    }

    public static IndexMap create(int startIndex, int count) {
        return new RangeBased(startIndex, count);
    }

    public abstract IndexMap add(int var1, int var2);

    public abstract int map(int var1);

    private static class RangeBased
    extends IndexMap {
        private int startIndex;
        private int count;

        RangeBased() {
        }

        RangeBased(int startIndex, int count) {
            Assertions.isTrueArgument("startIndex", startIndex >= 0);
            Assertions.isTrueArgument("count", count > 0);
            this.startIndex = startIndex;
            this.count = count;
        }

        @Override
        public IndexMap add(int index, int originalIndex) {
            if (this.count == 0) {
                this.startIndex = originalIndex;
                this.count = 1;
                return this;
            }
            if (originalIndex == this.startIndex + this.count) {
                ++this.count;
                return this;
            }
            HashBased hashBasedMap = new HashBased(this.startIndex, this.count);
            ((IndexMap)hashBasedMap).add(index, originalIndex);
            return hashBasedMap;
        }

        @Override
        public int map(int index) {
            if (index < 0) {
                throw new MongoInternalException("no mapping found for index " + index);
            }
            if (index >= this.count) {
                throw new MongoInternalException("index should not be greater than or equal to count");
            }
            return this.startIndex + index;
        }
    }

    private static class HashBased
    extends IndexMap {
        private final Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

        HashBased(int startIndex, int count) {
            for (int i2 = startIndex; i2 < startIndex + count; ++i2) {
                this.indexMap.put(i2 - startIndex, i2);
            }
        }

        @Override
        public IndexMap add(int index, int originalIndex) {
            this.indexMap.put(index, originalIndex);
            return this;
        }

        @Override
        public int map(int index) {
            Integer originalIndex = this.indexMap.get(index);
            if (originalIndex == null) {
                throw new MongoInternalException("no mapping found for index " + index);
            }
            return originalIndex;
        }
    }
}

