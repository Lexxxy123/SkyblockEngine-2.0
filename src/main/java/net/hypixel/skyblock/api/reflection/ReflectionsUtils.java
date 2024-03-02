package net.hypixel.skyblock.api.reflection;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ReflectionsUtils {


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

	
}