package vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.wrapper;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ConstructorWrapper<R> extends WrapperAbstract {
    private final Constructor<R> constructor;

    public ConstructorWrapper(final Constructor<R> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean exists() {
        return this.constructor != null;
    }

    public R newInstance(final Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public R newInstanceSilent(final Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (final Exception e) {
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
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final ConstructorWrapper<?> that = (ConstructorWrapper<?>) object;
        return Objects.equals(this.constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return (this.constructor != null) ? this.constructor.hashCode() : 0;
    }
}
