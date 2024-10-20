/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicensePatchData {
    private final String product;
    private final LocalDateTime expiration;
    private final Integer maxServers;
    private final Integer maxIps;
    private final String blacklistReason;
    private final String note;
    private final Map<String, String> connections;
    private final List<String> servers;
    private final List<String> ips;

    public static LicensePatchDataBuilder builder() {
        return new LicensePatchDataBuilder();
    }

    public String getProduct() {
        return this.product;
    }

    public LocalDateTime getExpiration() {
        return this.expiration;
    }

    public Integer getMaxServers() {
        return this.maxServers;
    }

    public Integer getMaxIps() {
        return this.maxIps;
    }

    public String getBlacklistReason() {
        return this.blacklistReason;
    }

    public String getNote() {
        return this.note;
    }

    public Map<String, String> getConnections() {
        return this.connections;
    }

    public List<String> getServers() {
        return this.servers;
    }

    public List<String> getIps() {
        return this.ips;
    }

    private LicensePatchData(String product, LocalDateTime expiration, Integer maxServers, Integer maxIps, String blacklistReason, String note, Map<String, String> connections, List<String> servers, List<String> ips) {
        this.product = product;
        this.expiration = expiration;
        this.maxServers = maxServers;
        this.maxIps = maxIps;
        this.blacklistReason = blacklistReason;
        this.note = note;
        this.connections = connections;
        this.servers = servers;
        this.ips = ips;
    }

    public String toString() {
        return "LicensePatchData(product=" + this.getProduct() + ", expiration=" + this.getExpiration() + ", maxServers=" + this.getMaxServers() + ", maxIps=" + this.getMaxIps() + ", blacklistReason=" + this.getBlacklistReason() + ", note=" + this.getNote() + ", connections=" + this.getConnections() + ", servers=" + this.getServers() + ", ips=" + this.getIps() + ")";
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof LicensePatchData)) {
            return false;
        }
        LicensePatchData other = (LicensePatchData)o2;
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
        String this$blacklistReason = this.getBlacklistReason();
        String other$blacklistReason = other.getBlacklistReason();
        if (this$blacklistReason == null ? other$blacklistReason != null : !this$blacklistReason.equals(other$blacklistReason)) {
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
        List<String> this$servers = this.getServers();
        List<String> other$servers = other.getServers();
        if (this$servers == null ? other$servers != null : !((Object)this$servers).equals(other$servers)) {
            return false;
        }
        List<String> this$ips = this.getIps();
        List<String> other$ips = other.getIps();
        return !(this$ips == null ? other$ips != null : !((Object)this$ips).equals(other$ips));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LicensePatchData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $maxServers = this.getMaxServers();
        result = result * 59 + ($maxServers == null ? 43 : ((Object)$maxServers).hashCode());
        Integer $maxIps = this.getMaxIps();
        result = result * 59 + ($maxIps == null ? 43 : ((Object)$maxIps).hashCode());
        String $product = this.getProduct();
        result = result * 59 + ($product == null ? 43 : $product.hashCode());
        LocalDateTime $expiration = this.getExpiration();
        result = result * 59 + ($expiration == null ? 43 : ((Object)$expiration).hashCode());
        String $blacklistReason = this.getBlacklistReason();
        result = result * 59 + ($blacklistReason == null ? 43 : $blacklistReason.hashCode());
        String $note = this.getNote();
        result = result * 59 + ($note == null ? 43 : $note.hashCode());
        Map<String, String> $connections = this.getConnections();
        result = result * 59 + ($connections == null ? 43 : ((Object)$connections).hashCode());
        List<String> $servers = this.getServers();
        result = result * 59 + ($servers == null ? 43 : ((Object)$servers).hashCode());
        List<String> $ips = this.getIps();
        result = result * 59 + ($ips == null ? 43 : ((Object)$ips).hashCode());
        return result;
    }

    public static class LicensePatchDataBuilder {
        private String product;
        private LocalDateTime expiration;
        private Integer maxServers;
        private Integer maxIps;
        private String blacklistReason;
        private String note;
        private Map<String, String> connections;
        private List<String> servers;
        private List<String> ips;

        public LicensePatchDataBuilder product(String product) {
            this.product = product;
            return this;
        }

        public LicensePatchDataBuilder expiration(LocalDateTime expiration) {
            this.expiration = expiration;
            return this;
        }

        public LicensePatchDataBuilder maxServers(Integer maxServers) {
            this.maxServers = maxServers;
            return this;
        }

        public LicensePatchDataBuilder maxIps(Integer maxIps) {
            this.maxIps = maxIps;
            return this;
        }

        public LicensePatchDataBuilder blacklistReason(String blacklistReason) {
            this.blacklistReason = blacklistReason;
            return this;
        }

        public LicensePatchDataBuilder note(String note) {
            this.note = note;
            return this;
        }

        public LicensePatchDataBuilder connections(Map<String, String> connections) {
            if (this.connections == null) {
                this.connections = new HashMap<String, String>();
            }
            this.connections.putAll(connections);
            return this;
        }

        public LicensePatchDataBuilder connection(String platform, String value) {
            if (this.connections == null) {
                this.connections = new HashMap<String, String>();
            }
            this.connections.put(platform, value);
            return this;
        }

        public LicensePatchDataBuilder servers(List<String> servers) {
            if (this.servers == null) {
                this.servers = new ArrayList<String>();
            }
            this.servers.addAll(servers);
            return this;
        }

        public LicensePatchDataBuilder server(String server) {
            if (this.servers == null) {
                this.servers = new ArrayList<String>();
            }
            this.servers.add(server);
            return this;
        }

        public LicensePatchDataBuilder ips(List<String> ips) {
            if (this.ips == null) {
                this.ips = new ArrayList<String>();
            }
            this.ips.addAll(ips);
            return this;
        }

        public LicensePatchDataBuilder ip(String ip) {
            if (this.ips == null) {
                this.ips = new ArrayList<String>();
            }
            this.ips.add(ip);
            return this;
        }

        public LicensePatchData build() {
            return new LicensePatchData(this.product, this.expiration, this.maxServers, this.maxIps, this.blacklistReason, this.note, this.connections, this.servers, this.ips);
        }
    }
}

