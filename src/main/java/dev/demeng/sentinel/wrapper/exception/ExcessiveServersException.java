/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;

public class ExcessiveServersException
extends ApiException {
    private final int maxServers;

    public ExcessiveServersException(Response response) {
        super(response);
        this.maxServers = response.getResult("maxServers", Integer.TYPE);
    }

    public int getMaxServers() {
        return this.maxServers;
    }
}

