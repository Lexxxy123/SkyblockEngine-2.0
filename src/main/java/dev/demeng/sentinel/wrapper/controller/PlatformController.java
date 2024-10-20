/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.controller;

import dev.demeng.sentinel.wrapper.controller.Controller;
import dev.demeng.sentinel.wrapper.exception.unchecked.UnexpectedResponseException;
import dev.demeng.sentinel.wrapper.http.RequestBuilder;
import dev.demeng.sentinel.wrapper.http.Response;
import dev.demeng.sentinel.wrapper.util.QueryBuilder;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class PlatformController
extends Controller {
    public PlatformController(@NotNull String baseUrl) {
        super(baseUrl + "/platforms", null);
    }

    @NotNull
    public String[] getAll() throws IOException {
        Response response = new RequestBuilder().query(new QueryBuilder(this.baseUrl).build()).build().getResponse();
        if (response.getType().equals("SUCCESS")) {
            return response.getResult("platforms", String[].class);
        }
        throw new UnexpectedResponseException(response);
    }
}

