/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception.unchecked;

import dev.demeng.sentinel.wrapper.http.Response;

public class UncheckedApiException
extends RuntimeException {
    private final transient Response response;

    public UncheckedApiException(Response response) {
        super(response.getStatus() + " - " + response.getType() + ": " + response.getMessage());
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }
}

