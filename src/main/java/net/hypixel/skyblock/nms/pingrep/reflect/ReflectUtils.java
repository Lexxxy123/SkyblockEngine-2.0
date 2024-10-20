/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.pingrep.reflect;

import java.lang.reflect.Field;

public class ReflectUtils {
    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() != type) continue;
            return field;
        }
        return null;
    }
}

