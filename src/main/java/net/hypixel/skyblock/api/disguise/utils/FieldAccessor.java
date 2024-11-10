/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.lang.reflect.Field;

public class FieldAccessor<T, K> {
    private Field field = null;

    public FieldAccessor(Class<T> instanceClass, Class<K> fieldClass) {
        for (Field f : instanceClass.getFields()) {
            if (!f.getDeclaringClass().equals(fieldClass)) continue;
            this.field = f;
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
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static class FiledNotFoundException
    extends RuntimeException {
        public FiledNotFoundException(String s) {
            super(s);
        }
    }
}

