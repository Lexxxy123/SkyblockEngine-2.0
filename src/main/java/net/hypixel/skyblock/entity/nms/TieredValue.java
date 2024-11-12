/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.nms;

public class TieredValue<T> {
    private final T i;
    private final T ii;
    private final T iii;
    private final T iv;

    public TieredValue(T i2, T ii, T iii, T iv) {
        this.i = i2;
        this.ii = ii;
        this.iii = iii;
        this.iv = iv;
    }

    public T getByNumber(int n2) {
        switch (n2) {
            case 2: {
                return this.ii;
            }
            case 3: {
                return this.iii;
            }
            case 4: {
                return this.iv;
            }
        }
        return this.i;
    }

    public T getTierI() {
        return this.i;
    }

    public T getTierII() {
        return this.ii;
    }

    public T getTierIII() {
        return this.iii;
    }

    public T getTierIV() {
        return this.iv;
    }
}

