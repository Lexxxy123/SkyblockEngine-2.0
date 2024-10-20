/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.util;

public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Pair)) {
            return false;
        }
        Pair other = (Pair)o2;
        if (!other.canEqual(this)) {
            return false;
        }
        K this$key = this.getKey();
        K other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        V this$value = this.getValue();
        V other$value = other.getValue();
        return !(this$value == null ? other$value != null : !this$value.equals(other$value));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Pair;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        K $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        V $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "Pair(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
}

