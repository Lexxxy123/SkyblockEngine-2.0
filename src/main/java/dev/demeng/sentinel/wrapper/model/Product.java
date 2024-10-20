/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.model;

public class Product {
    private final String name;
    private final int defaultMaxServers;
    private final int defaultMaxIps;
    private final boolean autoCreateLicenses;

    public Product(String name, int defaultMaxServers, int defaultMaxIps, boolean autoCreateLicenses) {
        this.name = name;
        this.defaultMaxServers = defaultMaxServers;
        this.defaultMaxIps = defaultMaxIps;
        this.autoCreateLicenses = autoCreateLicenses;
    }

    public String getName() {
        return this.name;
    }

    public int getDefaultMaxServers() {
        return this.defaultMaxServers;
    }

    public int getDefaultMaxIps() {
        return this.defaultMaxIps;
    }

    public boolean isAutoCreateLicenses() {
        return this.autoCreateLicenses;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Product)) {
            return false;
        }
        Product other = (Product)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getDefaultMaxServers() != other.getDefaultMaxServers()) {
            return false;
        }
        if (this.getDefaultMaxIps() != other.getDefaultMaxIps()) {
            return false;
        }
        if (this.isAutoCreateLicenses() != other.isAutoCreateLicenses()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Product;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getDefaultMaxServers();
        result = result * 59 + this.getDefaultMaxIps();
        result = result * 59 + (this.isAutoCreateLicenses() ? 79 : 97);
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Product(name=" + this.getName() + ", defaultMaxServers=" + this.getDefaultMaxServers() + ", defaultMaxIps=" + this.getDefaultMaxIps() + ", autoCreateLicenses=" + this.isAutoCreateLicenses() + ")";
    }
}

