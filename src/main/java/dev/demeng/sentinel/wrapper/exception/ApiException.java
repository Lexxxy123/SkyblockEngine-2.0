/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.http.Response;

public class ApiException
extends Exception {
    private final transient Response response;

    public ApiException(Response response) {
        super(response.getStatus() + " - " + response.getType() + ": " + response.getMessage());
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }
}

