/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;
import org.jetbrains.annotations.Nullable;

public class InvalidPlatformException
extends ApiException {
    @Nullable
    private final String platform;

    public InvalidPlatformException(Response response) {
        super(response);
        this.platform = response.getResult() != null && !response.getResult().isJsonNull() ? response.getResult().getAsJsonObject().get("platform").getAsString() : null;
    }

    @Nullable
    public String getPlatform() {
        return this.platform;
    }
}

