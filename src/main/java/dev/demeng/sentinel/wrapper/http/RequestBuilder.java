/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.http;

import dev.demeng.sentinel.wrapper.http.HttpMethod;
import dev.demeng.sentinel.wrapper.http.Request;

public class RequestBuilder {
    private HttpMethod method = HttpMethod.GET;
    private String apiKey;
    private String query;
    private Object body;

    public RequestBuilder method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RequestBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public RequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public Request build() {
        return new Request(this.method, this.apiKey, this.query, this.body);
    }
}

