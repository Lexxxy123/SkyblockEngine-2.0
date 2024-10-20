/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.controller;

import dev.demeng.sentinel.wrapper.controller.Controller;
import dev.demeng.sentinel.wrapper.exception.BlacklistedLicenseException;
import dev.demeng.sentinel.wrapper.exception.ConnectionAlreadyExistsException;
import dev.demeng.sentinel.wrapper.exception.ConnectionMismatchException;
import dev.demeng.sentinel.wrapper.exception.ExcessiveIpsException;
import dev.demeng.sentinel.wrapper.exception.ExcessiveServersException;
import dev.demeng.sentinel.wrapper.exception.ExpiredLicenseException;
import dev.demeng.sentinel.wrapper.exception.InvalidLicenseException;
import dev.demeng.sentinel.wrapper.exception.InvalidPlatformException;
import dev.demeng.sentinel.wrapper.exception.InvalidProductException;
import dev.demeng.sentinel.wrapper.exception.KeyAlreadyExistsException;
import dev.demeng.sentinel.wrapper.exception.NoResultsException;
import dev.demeng.sentinel.wrapper.exception.unchecked.UnauthorizedException;
import dev.demeng.sentinel.wrapper.exception.unchecked.UnexpectedResponseException;
import dev.demeng.sentinel.wrapper.http.HttpMethod;
import dev.demeng.sentinel.wrapper.http.RequestBuilder;
import dev.demeng.sentinel.wrapper.http.Response;
import dev.demeng.sentinel.wrapper.model.License;
import dev.demeng.sentinel.wrapper.model.LicenseCreateData;
import dev.demeng.sentinel.wrapper.model.LicensePatchData;
import dev.demeng.sentinel.wrapper.util.QueryBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LicenseController
extends Controller {
    public LicenseController(@NotNull String baseUrl, @NotNull String apiKey) {
        super(baseUrl + "/licenses", apiKey);
    }

    public void auth(@Nullable String key, @NotNull String product, @Nullable String connectionPlatform, @Nullable String connectionValue, @NotNull String server, @Nullable String ip) throws IOException, InvalidProductException, InvalidPlatformException, InvalidLicenseException, ExpiredLicenseException, BlacklistedLicenseException, ConnectionMismatchException, ExcessiveServersException, ExcessiveIpsException {
        Response response = new RequestBuilder().apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/auth").append("key", key).append("product", product).append("connectionPlatform", connectionPlatform).append("connectionValue", connectionValue).append("server", server).append("ip", ip).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                break;
            }
            case "UNAUTHORIZED": {
                throw new UnauthorizedException(response);
            }
            case "INVALID_PRODUCT": {
                throw new InvalidProductException(response);
            }
            case "INVALID_PLATFORM": {
                throw new InvalidPlatformException(response);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
            case "EXPIRED_LICENSE": {
                throw new ExpiredLicenseException(response);
            }
            case "BLACKLISTED_LICENSE": {
                throw new BlacklistedLicenseException(response);
            }
            case "CONNECTION_MISMATCH": {
                throw new ConnectionMismatchException(response);
            }
            case "EXCESSIVE_SERVERS": {
                throw new ExcessiveServersException(response);
            }
            case "EXCESSIVE_IPS": {
                throw new ExcessiveIpsException(response);
            }
            default: {
                throw new UnexpectedResponseException(response);
            }
        }
    }

    @NotNull
    public License search(@NotNull String product, @NotNull String platform, @NotNull String value) throws IOException, NoResultsException {
        Response response = new RequestBuilder().apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/search").append("product", product).append("platform", platform).append("value", value).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "UNAUTHORIZED": {
                throw new UnauthorizedException(response);
            }
            case "NO_RESULTS": {
                throw new NoResultsException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License[] search(@NotNull String platform, @NotNull String value) throws IOException, NoResultsException {
        Response response = new RequestBuilder().apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/search").append("platform", platform).append("value", value).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("licenses", License[].class);
            }
            case "UNAUTHORIZED": {
                throw new UnauthorizedException(response);
            }
            case "NO_RESULTS": {
                throw new NoResultsException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License create(@NotNull LicenseCreateData data) throws IOException, InvalidProductException, InvalidPlatformException, KeyAlreadyExistsException, ConnectionAlreadyExistsException {
        Response response = new RequestBuilder().method(HttpMethod.POST).apiKey(this.apiKey).query(this.baseUrl).body(data).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "UNAUTHORIZED": {
                throw new UnauthorizedException(response);
            }
            case "INVALID_PRODUCT": {
                throw new InvalidProductException(response);
            }
            case "INVALID_PLATFORM": {
                throw new InvalidPlatformException(response);
            }
            case "KEY_ALREADY_EXISTS": {
                throw new KeyAlreadyExistsException(response);
            }
            case "CONNECTION_ALREADY_EXISTS": {
                throw new ConnectionAlreadyExistsException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License get(@NotNull String key) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    public void delete(@NotNull String key) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().method(HttpMethod.DELETE).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return;
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License update(@NotNull String key, @NotNull LicensePatchData data) throws IOException, InvalidLicenseException, InvalidProductException, InvalidPlatformException, ConnectionAlreadyExistsException {
        Response response = new RequestBuilder().method(HttpMethod.PATCH).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key).build()).body(data).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
            case "INVALID_PRODUCT": {
                throw new InvalidProductException(response);
            }
            case "INVALID_PLATFORM": {
                throw new InvalidPlatformException(response);
            }
            case "CONNECTION_ALREADY_EXISTS": {
                throw new ConnectionAlreadyExistsException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License updateConnections(@NotNull String key, @NotNull Map<String, String> connections) throws IOException, InvalidLicenseException, InvalidPlatformException, ConnectionAlreadyExistsException {
        Response response = new RequestBuilder().method(HttpMethod.PUT).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key + "/connections").build()).body(connections).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
            case "INVALID_PLATFORM": {
                throw new InvalidPlatformException(response);
            }
            case "CONNECTION_ALREADY_EXISTS": {
                throw new ConnectionAlreadyExistsException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License deleteConnections(@NotNull String key, @NotNull String ... platforms) throws IOException, InvalidLicenseException {
        QueryBuilder query = new QueryBuilder(this.baseUrl + "/" + key + "/connections");
        for (String platform : platforms) {
            query.append("platforms", platform);
        }
        Response response = new RequestBuilder().method(HttpMethod.DELETE).apiKey(this.apiKey).query(query.build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License updateBlacklist(@NotNull String key, @NotNull String reason) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().method(HttpMethod.PUT).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key + "/blacklist").append("reason", reason).build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License deleteBlacklist(@NotNull String key) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().method(HttpMethod.DELETE).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key + "/blacklist").build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License updateServers(@NotNull String key, @NotNull List<String> servers) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().method(HttpMethod.PUT).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key + "/servers").build()).body(servers).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License deleteServers(@NotNull String key, @NotNull String ... servers) throws IOException, InvalidLicenseException {
        QueryBuilder query = new QueryBuilder(this.baseUrl + "/" + key + "/servers");
        for (String server : servers) {
            query.append("servers", server);
        }
        Response response = new RequestBuilder().method(HttpMethod.DELETE).apiKey(this.apiKey).query(query.build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License updateIps(@NotNull String key, @NotNull List<String> ips) throws IOException, InvalidLicenseException {
        Response response = new RequestBuilder().method(HttpMethod.PUT).apiKey(this.apiKey).query(new QueryBuilder(this.baseUrl + "/" + key + "/ips").build()).body(ips).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }

    @NotNull
    public License deleteIps(@NotNull String key, @NotNull String ... ips) throws IOException, InvalidLicenseException {
        QueryBuilder query = new QueryBuilder(this.baseUrl + "/" + key + "/ips");
        for (String ip : ips) {
            query.append("ips", ip);
        }
        Response response = new RequestBuilder().method(HttpMethod.DELETE).apiKey(this.apiKey).query(query.build()).build().getResponse();
        switch (response.getType()) {
            case "SUCCESS": {
                return response.getResult("license", License.class);
            }
            case "INVALID_LICENSE": {
                throw new InvalidLicenseException(response);
            }
        }
        throw new UnexpectedResponseException(response);
    }
}

