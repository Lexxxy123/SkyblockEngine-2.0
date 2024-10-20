/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;

public class ExcessiveIpsException
extends ApiException {
    private final int maxIps;

    public ExcessiveIpsException(Response response) {
        super(response);
        this.maxIps = response.getResult("maxIps", Integer.TYPE);
    }

    public int getMaxIps() {
        return this.maxIps;
    }
}

