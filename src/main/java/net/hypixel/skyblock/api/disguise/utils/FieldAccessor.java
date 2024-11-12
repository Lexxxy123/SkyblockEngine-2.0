/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.lang.reflect.Field;

public class FieldAccessor<T, K> {
    private Field field = null;

    public FieldAccessor(Class<T> instanceClass, Class<K> fieldClass) {
        for (Field f2 : instanceClass.getFields()) {
            if (!f2.getDeclaringClass().equals(fieldClass)) continue;
            this.field = f2;
            break;
        }
        if (this.field == null) {
            throw new FiledNotFoundException("No filed with this type " + fieldClass + " in class " + instanceClass);
        }
    }

    public Object get(Object object) {
        try {
            this.field.setAccessible(true);
            return this.field.get(object);
        } catch (Exception e2) {
            throw new RuntimeException();
        }
    }

    public static class FiledNotFoundException
    extends RuntimeException {
        public FiledNotFoundException(String s2) {
            super(s2);
        }
    }
}

