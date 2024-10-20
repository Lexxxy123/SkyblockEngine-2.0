/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.util;

public interface TriConsumer<T, U, V> {
    public void accept(T var1, U var2, V var3);

    default public TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        return null;
    }
}

