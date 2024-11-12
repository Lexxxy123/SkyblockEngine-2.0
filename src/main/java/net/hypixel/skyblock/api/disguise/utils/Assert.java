/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class Assert {
    public static <T> void notNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(Assert.nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(Assert.nullSafeGet(messageSupplier));
        }
    }

    public static <T extends Throwable> void notNull(Object object, T e2) throws T {
        if (object == null) {
            throw e2;
        }
    }

    public static void notNull(@Nullable Object object) {
        Assert.notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void isTrue(boolean expression) {
        Assert.isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new IllegalArgumentException(Assert.nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(@Nullable Object object) {
        Assert.isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalStateException(Assert.nullSafeGet(messageSupplier));
        }
    }

    public static void state(boolean expression) {
        Assert.state(expression, "[Assertion failed] - this state invariant must be true");
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }

    public static void allNotNull(String message, Object ... objects) {
        Assert.notNull((Object)objects, "objects should not be null!");
        for (Object o2 : objects) {
            Assert.notNull(o2, message);
        }
    }

    public static void isLarger(double base, double test) {
        Assert.isLarger(base, test, "[Assertion failed] - the value " + test + " has to be larger than " + base);
    }

    public static void isLarger(double base, double test, String message) {
        if (test <= base) {
            throw new IllegalStateException(message);
        }
    }

    public static void isSmaller(double base, double test) {
        Assert.isSmaller(base, test, "[Assertion failed] - the value " + test + " has to be smaller than " + base);
    }

    public static void isSmaller(double base, double test, String message) {
        if (test >= base) {
            throw new IllegalStateException(message);
        }
    }
}

