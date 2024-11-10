/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.nmsutil.reflection.resolver.minecraft;

import net.hypixel.skyblock.nms.nmsutil.reflection.minecraft.Minecraft;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.ClassResolver;

public class OBCClassResolver
extends ClassResolver {
    @Override
    public Class resolve(String ... names) throws ClassNotFoundException {
        for (int i = 0; i < names.length; ++i) {
            if (names[i].startsWith("org.bukkit.craftbukkit")) continue;
            names[i] = "org.bukkit.craftbukkit." + Minecraft.getVersion() + names[i];
        }
        return super.resolve(names);
    }
}

