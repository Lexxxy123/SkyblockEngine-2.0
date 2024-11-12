/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.nmsutil.reflection.resolver.wrapper;

import java.util.Objects;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.wrapper.WrapperAbstract;

public class ClassWrapper<R>
extends WrapperAbstract {
    private final Class<R> clazz;

    public ClassWrapper(Class<R> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean exists() {
        return null != this.clazz;
    }

    public Class<R> getClazz() {
        return this.clazz;
    }

    public String getName() {
        return this.clazz.getName();
    }

    public R newInstance() {
        try {
            return this.clazz.newInstance();
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public R newInstanceSilent() {
        try {
            return this.clazz.newInstance();
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object || this.getClass() != object.getClass()) {
            return false;
        }
        ClassWrapper that = (ClassWrapper)object;
        return Objects.equals(this.clazz, that.clazz);
    }

    public int hashCode() {
        return null != this.clazz ? this.clazz.hashCode() : 0;
    }
}

