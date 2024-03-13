package net.hypixel.skyblock.api.disguise.utils;

import java.lang.reflect.Field;

public class FieldAccessor<T, K> {
    private Field field = null;
    public FieldAccessor(Class<T> instanceClass, Class<K> fieldClass) {
        for (Field f : instanceClass.getFields()) {
            if(f.getDeclaringClass().equals(fieldClass)) {
                field = f;
                break;
            }
        }
        if(field == null)
            throw new FiledNotFoundException("No filed with this type " + fieldClass + " in class " + instanceClass);
    }
    public Object get(Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
    public static class FiledNotFoundException extends RuntimeException {
        public FiledNotFoundException(String s){
            super(s);
        }
    }
}
