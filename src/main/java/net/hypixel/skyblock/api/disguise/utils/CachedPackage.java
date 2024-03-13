//Source from ProtocolLib
package net.hypixel.skyblock.api.disguise.utils;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a dynamic package and an arbitrary number of cached classes.
 *
 * @author Kristian
 */
final class CachedPackage {

    private final String packageName;
    private final ClassSource source;
    private final Map<String, Optional<Class<?>>> cache;

    /**
     * Construct a new cached package.
     *
     * @param packageName - the name of the current package.
     * @param source      - the class source.
     */
    CachedPackage(String packageName, ClassSource source) {
        this.source = source;
        this.packageName = packageName;
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Correctly combine a package name and the child class we're looking for.
     *
     * @param packageName - name of the package, or an empty string for the default package.
     * @param className   - the class name.
     * @return We full class path.
     */
    public static String combine(String packageName, String className) {
        if (packageName == null || packageName.isEmpty()) {
            return className;
        } else {
            return packageName + "." + className;
        }
    }

    /**
     * Associate a given class with a class name.
     *
     * @param className - class name.
     * @param clazz     - type of class.
     */
    public void setPackageClass(String className, Class<?> clazz) {
        if (clazz != null) {
            this.cache.put(className, Optional.of(clazz));
        } else {
            this.cache.remove(className);
        }
    }

    private Optional<Class<?>> resolveClass(String className) {
        return source.loadClass(combine(packageName, className));
    }

    /**
     * Retrieve the class object of a specific class in the current package.
     *
     * @param className - the specific class.
     * @return Class object.
     * @throws RuntimeException If we are unable to find the given class.
     */
    public Optional<Class<?>> getPackageClass(String className, String... aliases) {
        return cache.computeIfAbsent(className, x -> {
            Optional<Class<?>> clazz = resolveClass(className);
            if (clazz.isPresent()) {
                return clazz;
            }

            for (String alias : aliases) {
                clazz = resolveClass(alias);
                if (clazz.isPresent()) {
                    return clazz;
                }
            }

            return Optional.empty();
        });
    }
}
