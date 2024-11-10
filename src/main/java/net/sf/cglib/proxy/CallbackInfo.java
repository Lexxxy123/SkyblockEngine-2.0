/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackGenerator;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.DispatcherGenerator;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.FixedValueGenerator;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.InvocationHandlerGenerator;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.LazyLoaderGenerator;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodInterceptorGenerator;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.proxy.NoOpGenerator;
import net.sf.cglib.proxy.ProxyRefDispatcher;
import org.objectweb.asm.Type;

class CallbackInfo {
    private Class cls;
    private CallbackGenerator generator;
    private Type type;
    private static final CallbackInfo[] CALLBACKS = new CallbackInfo[]{new CallbackInfo(NoOp.class, NoOpGenerator.INSTANCE), new CallbackInfo(MethodInterceptor.class, MethodInterceptorGenerator.INSTANCE), new CallbackInfo(InvocationHandler.class, InvocationHandlerGenerator.INSTANCE), new CallbackInfo(LazyLoader.class, LazyLoaderGenerator.INSTANCE), new CallbackInfo(Dispatcher.class, DispatcherGenerator.INSTANCE), new CallbackInfo(FixedValue.class, FixedValueGenerator.INSTANCE), new CallbackInfo(ProxyRefDispatcher.class, DispatcherGenerator.PROXY_REF_INSTANCE)};

    public static Type[] determineTypes(Class[] callbackTypes) {
        return CallbackInfo.determineTypes(callbackTypes, true);
    }

    public static Type[] determineTypes(Class[] callbackTypes, boolean checkAll) {
        Type[] types = new Type[callbackTypes.length];
        for (int i = 0; i < types.length; ++i) {
            types[i] = CallbackInfo.determineType(callbackTypes[i], checkAll);
        }
        return types;
    }

    public static Type[] determineTypes(Callback[] callbacks) {
        return CallbackInfo.determineTypes(callbacks, true);
    }

    public static Type[] determineTypes(Callback[] callbacks, boolean checkAll) {
        Type[] types = new Type[callbacks.length];
        for (int i = 0; i < types.length; ++i) {
            types[i] = CallbackInfo.determineType(callbacks[i], checkAll);
        }
        return types;
    }

    public static CallbackGenerator[] getGenerators(Type[] callbackTypes) {
        CallbackGenerator[] generators = new CallbackGenerator[callbackTypes.length];
        for (int i = 0; i < generators.length; ++i) {
            generators[i] = CallbackInfo.getGenerator(callbackTypes[i]);
        }
        return generators;
    }

    private CallbackInfo(Class cls, CallbackGenerator generator) {
        this.cls = cls;
        this.generator = generator;
        this.type = Type.getType(cls);
    }

    private static Type determineType(Callback callback, boolean checkAll) {
        if (callback == null) {
            throw new IllegalStateException("Callback is null");
        }
        return CallbackInfo.determineType(callback.getClass(), checkAll);
    }

    private static Type determineType(Class callbackType, boolean checkAll) {
        Class cur = null;
        Type type = null;
        for (int i = 0; i < CALLBACKS.length; ++i) {
            CallbackInfo info = CALLBACKS[i];
            if (!info.cls.isAssignableFrom(callbackType)) continue;
            if (cur != null) {
                throw new IllegalStateException("Callback implements both " + cur + " and " + info.cls);
            }
            cur = info.cls;
            type = info.type;
            if (!checkAll) break;
        }
        if (cur == null) {
            throw new IllegalStateException("Unknown callback type " + callbackType);
        }
        return type;
    }

    private static CallbackGenerator getGenerator(Type callbackType) {
        for (int i = 0; i < CALLBACKS.length; ++i) {
            CallbackInfo info = CALLBACKS[i];
            if (!info.type.equals(callbackType)) continue;
            return info.generator;
        }
        throw new IllegalStateException("Unknown callback type " + callbackType);
    }
}

