/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.model;

import dev.demeng.sentinel.wrapper.model.Blacklist;
import dev.demeng.sentinel.wrapper.model.Product;
import java.time.LocalDateTime;
import java.util.Map;

public class License {
    private final String key;
    private Product product;
    private final String issuer;
    private final LocalDateTime createdAt;
    private LocalDateTime expiration;
    private int maxServers;
    private int maxIps;
    private Blacklist blacklist;
    private String note;
    private Map<String, String> connections;
    private Map<String, LocalDateTime> servers;
    private Map<String, LocalDateTime> ips;

    public String getKey() {
        return this.key;
    }

    public Product getProduct() {
        return this.product;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getExpiration() {
        return this.expiration;
    }

    public int getMaxServers() {
        return this.maxServers;
    }

    public int getMaxIps() {
        return this.maxIps;
    }

    public Blacklist getBlacklist() {
        return this.blacklist;
    }

    public String getNote() {
        return this.note;
    }

    public Map<String, String> getConnections() {
        return this.connections;
    }

    public Map<String, LocalDateTime> getServers() {
        return this.servers;
    }

    public Map<String, LocalDateTime> getIps() {
        return this.ips;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public void setMaxServers(int maxServers) {
        this.maxServers = maxServers;
    }

    public void setMaxIps(int maxIps) {
        this.maxIps = maxIps;
    }

    public void setBlacklist(Blacklist blacklist) {
        this.blacklist = blacklist;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setConnections(Map<String, String> connections) {
        this.connections = connections;
    }

    public void setServers(Map<String, LocalDateTime> servers) {
        this.servers = servers;
    }

    public void setIps(Map<String, LocalDateTime> ips) {
        this.ips = ips;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof License)) {
            return false;
        }
        License other = (License)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getMaxServers() != other.getMaxServers()) {
            return false;
        }
        if (this.getMaxIps() != other.getMaxIps()) {
            return false;
        }
        String this$key = this.getKey();
        String other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        Product this$product = this.getProduct();
        Product other$product = other.getProduct();
        if (this$product == null ? other$product != null : !((Object)this$product).equals(other$product)) {
            return false;
        }
        String this$issuer = this.getIssuer();
        String other$issuer = other.getIssuer();
        if (this$issuer == null ? other$issuer != null : !this$issuer.equals(other$issuer)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$expiration = this.getExpiration();
        LocalDateTime other$expiration = other.getExpiration();
        if (this$expiration == null ? other$expiration != null : !((Object)this$expiration).equals(other$expiration)) {
            return false;
        }
        Blacklist this$blacklist = this.getBlacklist();
        Blacklist other$blacklist = other.getBlacklist();
        if (this$blacklist == null ? other$blacklist != null : !((Object)this$blacklist).equals(other$blacklist)) {
            return false;
        }
        String this$note = this.getNote();
        String other$note = other.getNote();
        if (this$note == null ? other$note != null : !this$note.equals(other$note)) {
            return false;
        }
        Map<String, String> this$connections = this.getConnections();
        Map<String, String> other$connections = other.getConnections();
        if (this$connections == null ? other$connections != null : !((Object)this$connections).equals(other$connections)) {
            return false;
        }
        Map<String, LocalDateTime> this$servers = this.getServers();
        Map<String, LocalDateTime> other$servers = other.getServers();
        if (this$servers == null ? other$servers != null : !((Object)this$servers).equals(other$servers)) {
            return false;
        }
        Map<String, LocalDateTime> this$ips = this.getIps();
        Map<String, LocalDateTime> other$ips = other.getIps();
        return !(this$ips == null ? other$ips != null : !((Object)this$ips).equals(other$ips));
    }

    protected boolean canEqual(Object other) {
        return other instanceof License;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getMaxServers();
        result = result * 59 + this.getMaxIps();
        String $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        Product $product = this.getProduct();
        result = result * 59 + ($product == null ? 43 : ((Object)$product).hashCode());
        String $issuer = this.getIssuer();
        result = result * 59 + ($issuer == null ? 43 : $issuer.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $expiration = this.getExpiration();
        result = result * 59 + ($expiration == null ? 43 : ((Object)$expiration).hashCode());
        Blacklist $blacklist = this.getBlacklist();
        result = result * 59 + ($blacklist == null ? 43 : ((Object)$blacklist).hashCode());
        String $note = this.getNote();
        result = result * 59 + ($note == null ? 43 : $note.hashCode());
        Map<String, String> $connections = this.getConnections();
        result = result * 59 + ($connections == null ? 43 : ((Object)$connections).hashCode());
        Map<String, LocalDateTime> $servers = this.getServers();
        result = result * 59 + ($servers == null ? 43 : ((Object)$servers).hashCode());
        Map<String, LocalDateTime> $ips = this.getIps();
        result = result * 59 + ($ips == null ? 43 : ((Object)$ips).hashCode());
        return result;
    }

    public String toString() {
        return "License(key=" + this.getKey() + ", product=" + this.getProduct() + ", issuer=" + this.getIssuer() + ", createdAt=" + this.getCreatedAt() + ", expiration=" + this.getExpiration() + ", maxServers=" + this.getMaxServers() + ", maxIps=" + this.getMaxIps() + ", blacklist=" + this.getBlacklist() + ", note=" + this.getNote() + ", connections=" + this.getConnections() + ", servers=" + this.getServers() + ", ips=" + this.getIps() + ")";
    }

    public License(String key, Product product, String issuer, LocalDateTime createdAt, LocalDateTime expiration, int maxServers, int maxIps, Blacklist blacklist, String note, Map<String, String> connections, Map<String, LocalDateTime> servers, Map<String, LocalDateTime> ips) {
        this.key = key;
        this.product = product;
        this.issuer = issuer;
        this.createdAt = createdAt;
        this.expiration = expiration;
        this.maxServers = maxServers;
        this.maxIps = maxIps;
        this.blacklist = blacklist;
        this.note = note;
        this.connections = connections;
        this.servers = servers;
        this.ips = ips;
    }
}

