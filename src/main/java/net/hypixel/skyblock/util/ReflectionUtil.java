/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.util;

import com.skyblock.skyblock.reflections.Reflections;
import com.skyblock.skyblock.reflections.scanners.Scanner;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ReflectionUtil {
    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName, new Scanner[0]);
        Set<Class<T>> subTypes = reflections.getSubTypesOf(clazz);
        return subTypes.stream().map(subClass -> {
            try {
                return clazz.cast(subClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                return null;
            }
        }).filter(Objects::nonNull);
    }
}

