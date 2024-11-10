/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.hypixel.skyblock.api.disguise.utils.ClassSource;

final class CachedPackage {
    private final String packageName;
    private final ClassSource source;
    private final Map<String, Optional<Class<?>>> cache;

    CachedPackage(String packageName, ClassSource source) {
        this.source = source;
        this.packageName = packageName;
        this.cache = new ConcurrentHashMap();
    }

    public static String combine(String packageName, String className) {
        if (packageName == null || packageName.isEmpty()) {
            return className;
        }
        return packageName + "." + className;
    }

    public void setPackageClass(String className, Class<?> clazz) {
        if (clazz != null) {
            this.cache.put(className, Optional.of(clazz));
        } else {
            this.cache.remove(className);
        }
    }

    private Optional<Class<?>> resolveClass(String className) {
        return this.source.loadClass(CachedPackage.combine(this.packageName, className));
    }

    public Optional<Class<?>> getPackageClass(String className, String ... aliases) {
        return this.cache.computeIfAbsent(className, x -> {
            Optional<Class<?>> clazz = this.resolveClass(className);
            if (clazz.isPresent()) {
                return clazz;
            }
            for (String alias : aliases) {
                clazz = this.resolveClass(alias);
                if (!clazz.isPresent()) continue;
                return clazz;
            }
            return Optional.empty();
        });
    }
}

