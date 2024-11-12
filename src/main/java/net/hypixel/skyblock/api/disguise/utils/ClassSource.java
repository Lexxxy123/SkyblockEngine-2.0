/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface ClassSource {
    public static ClassSource fromClassLoader() {
        return ClassSource.fromClassLoader(ClassSource.class.getClassLoader());
    }

    public static ClassSource fromPackage(String packageName) {
        return ClassSource.fromClassLoader().usingPackage(packageName);
    }

    public static ClassSource fromClassLoader(ClassLoader loader) {
        return canonicalName -> {
            try {
                return Optional.of(loader.loadClass(canonicalName));
            } catch (ClassNotFoundException ignored) {
                return Optional.empty();
            }
        };
    }

    public static ClassSource fromMap(Map<String, Class<?>> map) {
        return canonicalName -> Optional.ofNullable(map.get(canonicalName));
    }

    public static ClassSource empty() {
        return ClassSource.fromMap(Collections.emptyMap());
    }

    public static String append(String a2, String b2) {
        boolean left = a2.endsWith(".");
        boolean right = b2.endsWith(".");
        if (left && right) {
            return a2.substring(0, a2.length() - 1) + b2;
        }
        if (left != right) {
            return a2 + b2;
        }
        return a2 + "." + b2;
    }

    default public ClassSource usingPackage(String packageName) {
        return canonicalName -> this.loadClass(ClassSource.append(packageName, canonicalName));
    }

    public Optional<Class<?>> loadClass(String var1);
}

