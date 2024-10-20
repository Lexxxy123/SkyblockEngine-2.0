/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.nmsutil.reflection.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResolverQuery {
    private String name;
    private final Class<?>[] types;

    public ResolverQuery(String name, Class<?> ... types) {
        this.name = name;
        this.types = types;
    }

    public ResolverQuery(String name) {
        this.name = name;
        this.types = new Class[0];
    }

    public ResolverQuery(Class<?> ... types) {
        this.types = types;
    }

    public String getName() {
        return this.name;
    }

    public Class<?>[] getTypes() {
        return this.types;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ResolverQuery that = (ResolverQuery)o2;
        if (this.name != null ? this.name.equals(that.name) : that.name == null) {
            return Arrays.equals(this.types, that.types);
        }
        return false;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        result = 31 * result + (this.types != null ? Arrays.hashCode(this.types) : 0);
        return result;
    }

    public String toString() {
        return "ResolverQuery{name='" + this.name + '\'' + ", types=" + Arrays.toString(this.types) + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ResolverQuery> queryList = new ArrayList<ResolverQuery>();

        private Builder() {
        }

        public Builder with(String name, Class<?>[] types) {
            this.queryList.add(new ResolverQuery(name, types));
            return this;
        }

        public Builder with(String name) {
            this.queryList.add(new ResolverQuery(name));
            return this;
        }

        public Builder with(Class<?>[] types) {
            this.queryList.add(new ResolverQuery(types));
            return this;
        }

        public ResolverQuery[] build() {
            return this.queryList.toArray(new ResolverQuery[this.queryList.size()]);
        }
    }
}

