/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;

public class KeyAlreadyExistsException
extends ApiException {
    public KeyAlreadyExistsException(Response response) {
        super(response);
    }
}

