/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package dev.demeng.sentinel.wrapper.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.demeng.sentinel.wrapper.SentinelClient;

public class Response {
    private final long timestamp;
    private final String status;
    private final String type;
    private final String message;
    private final JsonObject result;

    public <T> T getResult(String key, Class<T> resultType) {
        if (this.result == null) {
            return null;
        }
        JsonElement element = this.result.get(key);
        if (element == null) {
            return null;
        }
        return (T)SentinelClient.GSON.fromJson(element, resultType);
    }

    public Response(long timestamp, String status, String type, String message, JsonObject result) {
        this.timestamp = timestamp;
        this.status = status;
        this.type = type;
        this.message = message;
        this.result = result;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getStatus() {
        return this.status;
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public JsonObject getResult() {
        return this.result;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Response)) {
            return false;
        }
        Response other = (Response)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTimestamp() != other.getTimestamp()) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        JsonObject this$result = this.getResult();
        JsonObject other$result = other.getResult();
        return !(this$result == null ? other$result != null : !this$result.equals(other$result));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Response;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $timestamp = this.getTimestamp();
        result = result * 59 + (int)($timestamp >>> 32 ^ $timestamp);
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        JsonObject $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    public String toString() {
        return "Response(timestamp=" + this.getTimestamp() + ", status=" + this.getStatus() + ", type=" + this.getType() + ", message=" + this.getMessage() + ", result=" + this.getResult() + ")";
    }
}

