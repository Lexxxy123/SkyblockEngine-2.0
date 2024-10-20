/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.nmsutil.reflection.resolver;

import java.lang.reflect.Member;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.ClassResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.ResolverAbstract;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.wrapper.WrapperAbstract;

public abstract class MemberResolver<T extends Member>
extends ResolverAbstract<T> {
    protected Class<?> clazz;

    public MemberResolver(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class cannot be null");
        }
        this.clazz = clazz;
    }

    public MemberResolver(String className) throws ClassNotFoundException {
        this(new ClassResolver().resolve(className));
    }

    public abstract T resolveIndex(int var1) throws IndexOutOfBoundsException, ReflectiveOperationException;

    public abstract T resolveIndexSilent(int var1);

    public abstract WrapperAbstract resolveIndexWrapper(int var1);
}

