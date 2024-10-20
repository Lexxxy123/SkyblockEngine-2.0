/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.reflection;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;

public class ReflectionsUtils {
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap(256);

    public static void setValue(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public static Object getValue(Object instance, String name) {
        Object result = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public static void setField(String name, Object obj, Object value) {
        ReflectionsUtils.setValue(obj, name, value);
    }

    public static Object getField(Field field, Object target) {
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException illegalAccessException) {
            throw new IllegalStateException("Should never get here");
        }
    }

    public static Object getField(String f2, Object target) {
        Field field = ReflectionsUtils.findField(target.getClass(), f2, null);
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException illegalAccessException) {
            throw new IllegalStateException("Should never get here");
        }
    }

    public static Field findField(Class<?> clazz, @Nullable String name, @Nullable Class<?> type) {
        for (Class<?> searchType = clazz; Object.class != searchType && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields;
            for (Field field : fields = ReflectionsUtils.getDeclaredFields(searchType)) {
                if (name != null && !name.equals(field.getName()) || type != null && !type.equals(field.getType())) continue;
                return field;
            }
        }
        return null;
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, result.length == 0 ? EMPTY_FIELD_ARRAY : result);
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }
        return result;
    }
}

