/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LicenseCreateData {
    @Nullable
    private final String key;
    @NotNull
    private final String product;
    @Nullable
    private final LocalDateTime expiration;
    @Nullable
    private final Integer maxServers;
    @Nullable
    private final Integer maxIps;
    @Nullable
    private final String note;
    @Nullable
    private final Map<String, String> connections;

    public static LicenseCreateDataBuilder builder() {
        return new LicenseCreateDataBuilder();
    }

    @Nullable
    public String getKey() {
        return this.key;
    }

    @NotNull
    public String getProduct() {
        return this.product;
    }

    @Nullable
    public LocalDateTime getExpiration() {
        return this.expiration;
    }

    @Nullable
    public Integer getMaxServers() {
        return this.maxServers;
    }

    @Nullable
    public Integer getMaxIps() {
        return this.maxIps;
    }

    @Nullable
    public String getNote() {
        return this.note;
    }

    @Nullable
    public Map<String, String> getConnections() {
        return this.connections;
    }

    private LicenseCreateData(@Nullable String key, @NotNull String product, @Nullable LocalDateTime expiration, @Nullable Integer maxServers, @Nullable Integer maxIps, @Nullable String note, @Nullable Map<String, String> connections) {
        if (product == null) {
            throw new NullPointerException("product is marked non-null but is null");
        }
        this.key = key;
        this.product = product;
        this.expiration = expiration;
        this.maxServers = maxServers;
        this.maxIps = maxIps;
        this.note = note;
        this.connections = connections;
    }

    public String toString() {
        return "LicenseCreateData(key=" + this.getKey() + ", product=" + this.getProduct() + ", expiration=" + this.getExpiration() + ", maxServers=" + this.getMaxServers() + ", maxIps=" + this.getMaxIps() + ", note=" + this.getNote() + ", connections=" + this.getConnections() + ")";
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof LicenseCreateData)) {
            return false;
        }
        LicenseCreateData other = (LicenseCreateData)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$maxServers = this.getMaxServers();
        Integer other$maxServers = other.getMaxServers();
        if (this$maxServers == null ? other$maxServers != null : !((Object)this$maxServers).equals(other$maxServers)) {
            return false;
        }
        Integer this$maxIps = this.getMaxIps();
        Integer other$maxIps = other.getMaxIps();
        if (this$maxIps == null ? other$maxIps != null : !((Object)this$maxIps).equals(other$maxIps)) {
            return false;
        }
        String this$key = this.getKey();
        String other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        String this$product = this.getProduct();
        String other$product = other.getProduct();
        if (this$product == null ? other$product != null : !this$product.equals(other$product)) {
            return false;
        }
        LocalDateTime this$expiration = this.getExpiration();
        LocalDateTime other$expiration = other.getExpiration();
        if (this$expiration == null ? other$expiration != null : !((Object)this$expiration).equals(other$expiration)) {
            return false;
        }
        String this$note = this.getNote();
        String other$note = other.getNote();
        if (this$note == null ? other$note != null : !this$note.equals(other$note)) {
            return false;
        }
        Map<String, String> this$connections = this.getConnections();
        Map<String, String> other$connections = other.getConnections();
        return !(this$connections == null ? other$connections != null : !((Object)this$connections).equals(other$connections));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LicenseCreateData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $maxServers = this.getMaxServers();
        result = result * 59 + ($maxServers == null ? 43 : ((Object)$maxServers).hashCode());
        Integer $maxIps = this.getMaxIps();
        result = result * 59 + ($maxIps == null ? 43 : ((Object)$maxIps).hashCode());
        String $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        String $product = this.getProduct();
        result = result * 59 + ($product == null ? 43 : $product.hashCode());
        LocalDateTime $expiration = this.getExpiration();
        result = result * 59 + ($expiration == null ? 43 : ((Object)$expiration).hashCode());
        String $note = this.getNote();
        result = result * 59 + ($note == null ? 43 : $note.hashCode());
        Map<String, String> $connections = this.getConnections();
        result = result * 59 + ($connections == null ? 43 : ((Object)$connections).hashCode());
        return result;
    }

    public static class LicenseCreateDataBuilder {
        private String key;
        private String product;
        private LocalDateTime expiration;
        private Integer maxServers;
        private Integer maxIps;
        private String note;
        private Map<String, String> connections;

        public LicenseCreateDataBuilder key(String key) {
            this.key = key;
            return this;
        }

        public LicenseCreateDataBuilder product(String product) {
            this.product = product;
            return this;
        }

        public LicenseCreateDataBuilder expiration(LocalDateTime expiration) {
            this.expiration = expiration;
            return this;
        }

        public LicenseCreateDataBuilder maxServers(Integer maxServers) {
            this.maxServers = maxServers;
            return this;
        }

        public LicenseCreateDataBuilder maxIps(Integer maxIps) {
            this.maxIps = maxIps;
            return this;
        }

        public LicenseCreateDataBuilder note(String note) {
            this.note = note;
            return this;
        }

        public LicenseCreateDataBuilder connections(Map<String, String> connections) {
            if (this.connections == null) {
                this.connections = new HashMap<String, String>();
            }
            this.connections.putAll(connections);
            return this;
        }

        public LicenseCreateDataBuilder connection(String platform, String value) {
            if (this.connections == null) {
                this.connections = new HashMap<String, String>();
            }
            this.connections.put(platform, value);
            return this;
        }

        public LicenseCreateData build() {
            Objects.requireNonNull(this.product, "Product is null");
            return new LicenseCreateData(this.key, this.product, this.expiration, this.maxServers, this.maxIps, this.note, this.connections);
        }
    }
}

