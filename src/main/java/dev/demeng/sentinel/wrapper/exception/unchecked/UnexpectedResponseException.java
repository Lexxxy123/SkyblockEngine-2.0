/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception.unchecked;

import dev.demeng.sentinel.wrapper.exception.unchecked.UncheckedApiException;
import dev.demeng.sentinel.wrapper.http.Response;

public class UnexpectedResponseException
extends UncheckedApiException {
    public UnexpectedResponseException(Response response) {
        super(response);
    }
}

