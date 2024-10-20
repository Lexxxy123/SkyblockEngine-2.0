/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.TagSet;
import com.mongodb.TaggableReadPreference;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonString;

@Immutable
public abstract class ReadPreference {
    private static final ReadPreference PRIMARY = new PrimaryReadPreference();
    private static final ReadPreference SECONDARY = new TaggableReadPreference.SecondaryReadPreference();
    private static final ReadPreference SECONDARY_PREFERRED = new TaggableReadPreference.SecondaryPreferredReadPreference();
    private static final ReadPreference PRIMARY_PREFERRED = new TaggableReadPreference.PrimaryPreferredReadPreference();
    private static final ReadPreference NEAREST = new TaggableReadPreference.NearestReadPreference();

    ReadPreference() {
    }

    public abstract boolean isSlaveOk();

    public abstract String getName();

    public abstract BsonDocument toDocument();

    public final List<ServerDescription> choose(ClusterDescription clusterDescription) {
        switch (clusterDescription.getType()) {
            case REPLICA_SET: {
                return this.chooseForReplicaSet(clusterDescription);
            }
            case SHARDED: 
            case STANDALONE: {
                return this.chooseForNonReplicaSet(clusterDescription);
            }
            case UNKNOWN: {
                return Collections.emptyList();
            }
        }
        throw new UnsupportedOperationException("Unsupported cluster type: " + (Object)((Object)clusterDescription.getType()));
    }

    protected abstract List<ServerDescription> chooseForNonReplicaSet(ClusterDescription var1);

    protected abstract List<ServerDescription> chooseForReplicaSet(ClusterDescription var1);

    public static ReadPreference primary() {
        return PRIMARY;
    }

    public static ReadPreference primaryPreferred() {
        return PRIMARY_PREFERRED;
    }

    public static ReadPreference secondary() {
        return SECONDARY;
    }

    public static ReadPreference secondaryPreferred() {
        return SECONDARY_PREFERRED;
    }

    public static ReadPreference nearest() {
        return NEAREST;
    }

    public static ReadPreference primaryPreferred(long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.<TagSet>emptyList(), maxStaleness, timeUnit);
    }

    public static ReadPreference secondary(long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryReadPreference(Collections.<TagSet>emptyList(), maxStaleness, timeUnit);
    }

    public static ReadPreference secondaryPreferred(long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.<TagSet>emptyList(), maxStaleness, timeUnit);
    }

    public static ReadPreference nearest(long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.NearestReadPreference(Collections.<TagSet>emptyList(), maxStaleness, timeUnit);
    }

    public static TaggableReadPreference primaryPreferred(TagSet tagSet) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.singletonList(tagSet), null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference secondary(TagSet tagSet) {
        return new TaggableReadPreference.SecondaryReadPreference(Collections.singletonList(tagSet), null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference secondaryPreferred(TagSet tagSet) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.singletonList(tagSet), null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference nearest(TagSet tagSet) {
        return new TaggableReadPreference.NearestReadPreference(Collections.singletonList(tagSet), null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference primaryPreferred(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
    }

    public static TaggableReadPreference secondary(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
    }

    public static TaggableReadPreference secondaryPreferred(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
    }

    public static TaggableReadPreference nearest(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.NearestReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
    }

    public static TaggableReadPreference primaryPreferred(List<TagSet> tagSetList) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference secondary(List<TagSet> tagSetList) {
        return new TaggableReadPreference.SecondaryReadPreference(tagSetList, null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference secondaryPreferred(List<TagSet> tagSetList) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference nearest(List<TagSet> tagSetList) {
        return new TaggableReadPreference.NearestReadPreference(tagSetList, null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference primaryPreferred(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
    }

    public static TaggableReadPreference secondary(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryReadPreference(tagSetList, maxStaleness, timeUnit);
    }

    public static TaggableReadPreference secondaryPreferred(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
    }

    public static TaggableReadPreference nearest(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
        return new TaggableReadPreference.NearestReadPreference(tagSetList, maxStaleness, timeUnit);
    }

    public static ReadPreference valueOf(String name) {
        Assertions.notNull("name", name);
        String nameToCheck = name.toLowerCase();
        if (nameToCheck.equals(PRIMARY.getName().toLowerCase())) {
            return PRIMARY;
        }
        if (nameToCheck.equals(SECONDARY.getName().toLowerCase())) {
            return SECONDARY;
        }
        if (nameToCheck.equals(SECONDARY_PREFERRED.getName().toLowerCase())) {
            return SECONDARY_PREFERRED;
        }
        if (nameToCheck.equals(PRIMARY_PREFERRED.getName().toLowerCase())) {
            return PRIMARY_PREFERRED;
        }
        if (nameToCheck.equals(NEAREST.getName().toLowerCase())) {
            return NEAREST;
        }
        throw new IllegalArgumentException("No match for read preference of " + name);
    }

    public static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList) {
        return ReadPreference.valueOf(name, tagSetList, null, TimeUnit.MILLISECONDS);
    }

    public static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
        return ReadPreference.valueOf(name, tagSetList, (Long)maxStaleness, timeUnit);
    }

    private static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
        Assertions.notNull("name", name);
        Assertions.notNull("tagSetList", tagSetList);
        Assertions.notNull("timeUnit", timeUnit);
        String nameToCheck = name.toLowerCase();
        if (nameToCheck.equals(PRIMARY.getName().toLowerCase())) {
            throw new IllegalArgumentException("Primary read preference can not also specify tag sets or max staleness");
        }
        if (nameToCheck.equals(SECONDARY.getName().toLowerCase())) {
            return new TaggableReadPreference.SecondaryReadPreference(tagSetList, maxStaleness, timeUnit);
        }
        if (nameToCheck.equals(SECONDARY_PREFERRED.getName().toLowerCase())) {
            return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
        }
        if (nameToCheck.equals(PRIMARY_PREFERRED.getName().toLowerCase())) {
            return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
        }
        if (nameToCheck.equals(NEAREST.getName().toLowerCase())) {
            return new TaggableReadPreference.NearestReadPreference(tagSetList, maxStaleness, timeUnit);
        }
        throw new IllegalArgumentException("No match for read preference of " + name);
    }

    private static final class PrimaryReadPreference
    extends ReadPreference {
        private PrimaryReadPreference() {
        }

        @Override
        public boolean isSlaveOk() {
            return false;
        }

        public String toString() {
            return this.getName();
        }

        public boolean equals(Object o2) {
            return o2 != null && this.getClass() == o2.getClass();
        }

        public int hashCode() {
            return this.getName().hashCode();
        }

        @Override
        public BsonDocument toDocument() {
            return new BsonDocument("mode", new BsonString(this.getName()));
        }

        @Override
        protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
            return clusterDescription.getPrimaries();
        }

        @Override
        protected List<ServerDescription> chooseForNonReplicaSet(ClusterDescription clusterDescription) {
            return clusterDescription.getAny();
        }

        @Override
        public String getName() {
            return "primary";
        }
    }
}

