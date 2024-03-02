package net.hypixel.skyblock.nms.nmsutil.reflection.resolver.wrapper;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ConstructorWrapper<R> extends WrapperAbstract {
    private final Constructor<R> constructor;

    public ConstructorWrapper(Constructor<R> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean exists() {
        return null != this.constructor;
    }

    public R newInstance(Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public R newInstanceSilent(Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }

    public Class<?>[] getParameterTypes() {
        return this.constructor.getParameterTypes();
    }

    public Constructor<R> getConstructor() {
        return this.constructor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object || this.getClass() != object.getClass()) {
            return false;
        }
        ConstructorWrapper<?> that = (ConstructorWrapper<?>) object;
        return Objects.equals(this.constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return (null != this.constructor) ? this.constructor.hashCode() : 0;
    }
}
