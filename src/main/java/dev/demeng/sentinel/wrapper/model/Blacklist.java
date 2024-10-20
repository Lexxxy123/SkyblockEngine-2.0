/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.model;

import java.time.LocalDateTime;

public class Blacklist {
    private final LocalDateTime timestamp;
    private final String reason;

    public Blacklist(LocalDateTime timestamp, String reason) {
        this.timestamp = timestamp;
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getReason() {
        return this.reason;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Blacklist)) {
            return false;
        }
        Blacklist other = (Blacklist)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        LocalDateTime this$timestamp = this.getTimestamp();
        LocalDateTime other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        return !(this$reason == null ? other$reason != null : !this$reason.equals(other$reason));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Blacklist;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        LocalDateTime $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        return result;
    }

    public String toString() {
        return "Blacklist(timestamp=" + this.getTimestamp() + ", reason=" + this.getReason() + ")";
    }
}

