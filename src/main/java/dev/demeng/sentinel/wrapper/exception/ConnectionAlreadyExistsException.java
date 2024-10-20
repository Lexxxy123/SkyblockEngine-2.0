/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.reflect.TypeToken
 */
package dev.demeng.sentinel.wrapper.exception;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import dev.demeng.sentinel.wrapper.SentinelClient;
import dev.demeng.sentinel.wrapper.exception.ApiException;
import dev.demeng.sentinel.wrapper.http.Response;
import java.lang.reflect.Type;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ConnectionAlreadyExistsException
extends ApiException {
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    @NotNull
    private final String connectionPlatform;
    @NotNull
    private final String connectionValue;

    public ConnectionAlreadyExistsException(Response response) {
        super(response);
        JsonElement element = response.getResult().get("connection");
        Map map = (Map)SentinelClient.GSON.fromJson(element, MAP_TYPE);
        Map.Entry connection = map.entrySet().iterator().next();
        this.connectionPlatform = (String)connection.getKey();
        this.connectionValue = (String)connection.getValue();
    }

    @NotNull
    public String getConnectionPlatform() {
        return this.connectionPlatform;
    }

    @NotNull
    public String getConnectionValue() {
        return this.connectionValue;
    }
}

