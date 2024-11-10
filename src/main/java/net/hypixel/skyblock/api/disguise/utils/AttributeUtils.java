/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.entity.LivingEntity
 */
package net.hypixel.skyblock.api.disguise.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class AttributeUtils {
    private static Method getAttributeMethod;

    public static double getAttribute(LivingEntity entity, Attribute attribute) {
        if (getAttributeMethod != null) {
            try {
                Object attributeInstance = getAttributeMethod.invoke(entity, attribute);
                if (attributeInstance != null) {
                    Method getValueMethod = attributeInstance.getClass().getMethod("getValue", new Class[0]);
                    return (Double)getValueMethod.invoke(attributeInstance, new Object[0]);
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    static {
        try {
            Class<?> craftLivingEntityClass = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".entity.CraftLivingEntity");
            getAttributeMethod = craftLivingEntityClass.getDeclaredMethod("getAttribute", Attribute.class);
            getAttributeMethod.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}

