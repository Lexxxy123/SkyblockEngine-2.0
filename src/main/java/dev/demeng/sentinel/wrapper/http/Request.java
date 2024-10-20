/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package dev.demeng.sentinel.wrapper.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.demeng.sentinel.wrapper.SentinelClient;
import dev.demeng.sentinel.wrapper.http.HttpMethod;
import dev.demeng.sentinel.wrapper.http.Response;
import dev.demeng.sentinel.wrapper.http.ResponseDecrypter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class Request {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
    private static ResponseDecrypter decrypter;
    private final HttpMethod method;
    private final String apiKey;
    private final String query;
    private final Object body;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Response getResponse() throws IOException {
        Response response;
        String strResponse;
        block15: {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(this.escape(this.query));
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", USER_AGENT);
                if (this.method == HttpMethod.PATCH) {
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                } else {
                    connection.setRequestMethod(this.method.name());
                }
                if (this.apiKey != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + this.apiKey);
                }
                if (this.body != null) {
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    byte[] out = SentinelClient.GSON.toJson(this.body).getBytes(StandardCharsets.UTF_8);
                    OutputStream stream = connection.getOutputStream();
                    stream.write(out);
                }
                strResponse = this.readStream(connection.getInputStream());
            } catch (IOException ex) {
                if (connection != null && connection.getErrorStream() != null) {
                    strResponse = this.readStream(connection.getErrorStream());
                    break block15;
                }
                throw ex;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        if (strResponse.isEmpty()) {
            throw new IOException("Empty request response");
        }
        try {
            String decryptedResponse = strResponse;
            if (decrypter != null) {
                decryptedResponse = decrypter.decrypt(Base64.getDecoder().decode(strResponse));
            }
            JsonObject json = this.attemptParseString(decryptedResponse);
            long timestamp = json.get("timestamp").getAsLong();
            String status = json.get("status").getAsString();
            String type = json.get("type").getAsString();
            String message = json.get("message").getAsString();
            JsonElement resultElement = json.get("result");
            response = resultElement == null || resultElement.isJsonNull() ? new Response(timestamp, status, type, message, null) : new Response(timestamp, status, type, message, resultElement.getAsJsonObject());
        } catch (Exception ex) {
            throw new IOException("Failed to parse response: " + strResponse, ex);
        }
        return response;
    }

    private String readStream(InputStream stream) throws IOException {
        ArrayList<String> content = new ArrayList<String>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream));){
            String line;
            while ((line = in.readLine()) != null) {
                content.add(line);
            }
        }
        return String.join((CharSequence)"\n", content);
    }

    private String escape(String str) {
        try {
            URL url = new URL(str);
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
        } catch (MalformedURLException | URISyntaxException ex) {
            return str;
        }
    }

    private JsonObject attemptParseString(String str) {
        try {
            return JsonParser.parseString((String)str).getAsJsonObject();
        } catch (NoSuchMethodError ex) {
            return SentinelClient.LEGACY_PARSER.parse(str).getAsJsonObject();
        }
    }

    public Request(HttpMethod method, String apiKey, String query, Object body) {
        this.method = method;
        this.apiKey = apiKey;
        this.query = query;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getQuery() {
        return this.query;
    }

    public Object getBody() {
        return this.body;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Request)) {
            return false;
        }
        Request other = (Request)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        HttpMethod this$method = this.getMethod();
        HttpMethod other$method = other.getMethod();
        if (this$method == null ? other$method != null : !((Object)((Object)this$method)).equals((Object)other$method)) {
            return false;
        }
        String this$apiKey = this.getApiKey();
        String other$apiKey = other.getApiKey();
        if (this$apiKey == null ? other$apiKey != null : !this$apiKey.equals(other$apiKey)) {
            return false;
        }
        String this$query = this.getQuery();
        String other$query = other.getQuery();
        if (this$query == null ? other$query != null : !this$query.equals(other$query)) {
            return false;
        }
        Object this$body = this.getBody();
        Object other$body = other.getBody();
        return !(this$body == null ? other$body != null : !this$body.equals(other$body));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Request;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        HttpMethod $method = this.getMethod();
        result = result * 59 + ($method == null ? 43 : ((Object)((Object)$method)).hashCode());
        String $apiKey = this.getApiKey();
        result = result * 59 + ($apiKey == null ? 43 : $apiKey.hashCode());
        String $query = this.getQuery();
        result = result * 59 + ($query == null ? 43 : $query.hashCode());
        Object $body = this.getBody();
        result = result * 59 + ($body == null ? 43 : $body.hashCode());
        return result;
    }

    public String toString() {
        return "Request(method=" + (Object)((Object)this.getMethod()) + ", apiKey=" + this.getApiKey() + ", query=" + this.getQuery() + ", body=" + this.getBody() + ")";
    }

    public static void setDecrypter(ResponseDecrypter decrypter) {
        Request.decrypter = decrypter;
    }
}

