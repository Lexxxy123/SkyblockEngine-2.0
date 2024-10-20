/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.exception;

import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;
import dev.demeng.sentinel.wrapper.model.Blacklist;
import org.jetbrains.annotations.NotNull;

public class BlacklistedLicenseException
extends ApiException {
    @NotNull
    private final Blacklist blacklist;

    public BlacklistedLicenseException(Response response) {
        super(response);
        this.blacklist = response.getResult("blacklist", Blacklist.class);
    }

    @NotNull
    public Blacklist getBlacklist() {
        return this.blacklist;
    }
}

