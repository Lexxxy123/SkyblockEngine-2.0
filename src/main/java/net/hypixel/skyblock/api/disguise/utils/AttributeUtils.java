package net.hypixel.skyblock.api.disguise.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AttributeUtils {

    private static Method getAttributeMethod;

    static {
        try {
            Class<?> craftLivingEntityClass = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".entity.CraftLivingEntity");
            getAttributeMethod = craftLivingEntityClass.getDeclaredMethod("getAttribute", org.bukkit.attribute.Attribute.class);
            getAttributeMethod.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static double getAttribute(LivingEntity entity, Attribute attribute) {
        if (getAttributeMethod != null) {
            try {
                Object attributeInstance = getAttributeMethod.invoke(entity, attribute);
                if (attributeInstance != null) {
                    Method getValueMethod = attributeInstance.getClass().getMethod("getValue");
                    return (double) getValueMethod.invoke(attributeInstance);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }
}

