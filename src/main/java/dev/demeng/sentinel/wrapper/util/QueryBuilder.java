/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.util;

import org.jetbrains.annotations.Nullable;

public class QueryBuilder {
    private final String baseUrl;
    private final StringBuilder sb = new StringBuilder();

    public QueryBuilder append(@Nullable String key, @Nullable String value) {
        if (key == null || value == null) {
            return this;
        }
        if (this.sb.length() != 0) {
            this.sb.append("&");
        }
        this.sb.append(key).append("=").append(value);
        return this;
    }

    public String build() {
        return this.sb.length() == 0 ? this.baseUrl : this.baseUrl + "?" + this.sb;
    }

    public QueryBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

