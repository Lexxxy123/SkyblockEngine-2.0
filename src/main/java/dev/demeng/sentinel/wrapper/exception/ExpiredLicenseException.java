/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;

public class ExpiredLicenseException
extends ApiException {
    public ExpiredLicenseException(Response response) {
        super(response);
    }
}

