/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonParser
 */
package dev.demeng.sentinel.wrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import dev.demeng.sentinel.wrapper.controller.LicenseController;
import dev.demeng.sentinel.wrapper.controller.PlatformController;
import dev.demeng.sentinel.wrapper.controller.ProductController;
import dev.demeng.sentinel.wrapper.http.Request;
import dev.demeng.sentinel.wrapper.http.ResponseDecrypter;
import dev.demeng.sentinel.wrapper.serialize.LocalDateTimeSerializer;
import dev.demeng.sentinel.wrapper.util.Hwid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SentinelClient {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (Object)new LocalDateTimeSerializer()).create();
    public static final JsonParser LEGACY_PARSER = new JsonParser();
    @NotNull
    private final PlatformController platformController;
    @NotNull
    private final ProductController productController;
    @NotNull
    private final LicenseController licenseController;

    public SentinelClient(@NotNull String baseUrl, @NotNull String apiKey, @Nullable String secretKey) {
        if (secretKey != null) {
            Request.setDecrypter(new ResponseDecrypter(secretKey));
        }
        this.platformController = new PlatformController(baseUrl);
        this.productController = new ProductController(baseUrl);
        this.licenseController = new LicenseController(baseUrl, apiKey);
    }

    public SentinelClient(@NotNull String baseUrl, @NotNull String apiKey) {
        this(baseUrl, apiKey, null);
    }

    @NotNull
    public static String getCurrentHwid() {
        return Hwid.getHwid();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    public static String getCurrentIp() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));){
            String string = reader.readLine();
            return string;
        } catch (IOException ex) {
            return null;
        }
    }

    @NotNull
    public PlatformController getPlatformController() {
        return this.platformController;
    }

    @NotNull
    public ProductController getProductController() {
        return this.productController;
    }

    @NotNull
    public LicenseController getLicenseController() {
        return this.licenseController;
    }
}

