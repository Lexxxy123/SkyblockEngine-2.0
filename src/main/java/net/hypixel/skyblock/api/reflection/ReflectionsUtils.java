package net.hypixel.skyblock.api.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.v1_8_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class ReflectionsUtils {


	private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
	private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<>(256);

	public static void setValue(Object obj,String name,Object value){
		try{
		Field field = obj.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(obj, value);
		}catch(Exception e){}
	}
	
	public static Object getValue(Object obj,String name){
		try{
		Field field = obj.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return field.get(obj);
		}catch(Exception e){}
		return null;
	}



	public static void setField(String name , Object obj , Object value){
		setValue(obj , name , value);
	}


	public static Object getField(Field field, Object target) {
		field.setAccessible(true);
		try {
			return field.get(target);
		}
		catch (IllegalAccessException ex) {

		}
		throw new IllegalStateException("Should never get here");
	}



	public static Object getField(String f, Object target) {
		Field field = findField(target.getClass(), f , null);
		field.setAccessible(true);
		try {
			return field.get(target);
		}
		catch (IllegalAccessException ex) {
		}
		throw new IllegalStateException("Should never get here");
	}


	public static Field findField(Class<?> clazz, @Nullable String name, @Nullable Class<?> type) {
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) &&
						(type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	private static Field[] getDeclaredFields(Class<?> clazz) {
		Field[] result = declaredFieldsCache.get(clazz);
		if (result == null) {
			try {
				result = clazz.getDeclaredFields();
				declaredFieldsCache.put(clazz, (result.length == 0 ? EMPTY_FIELD_ARRAY : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
						"] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}
}