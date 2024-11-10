/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public abstract class BeanCopier {
    private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey)((Object)KeyFactory.create(BeanCopierKey.class));
    private static final Type CONVERTER = TypeUtils.parseType("net.sf.cglib.core.Converter");
    private static final Type BEAN_COPIER = TypeUtils.parseType("net.sf.cglib.beans.BeanCopier");
    private static final Signature COPY = new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
    private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");

    public static BeanCopier create(Class source, Class target, boolean useConverter) {
        Generator gen = new Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    public abstract void copy(Object var1, Object var2, Converter var3);

    public static class Generator
    extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(BeanCopier.class.getName());
        private Class source;
        private Class target;
        private boolean useConverter;

        public Generator() {
            super(SOURCE);
        }

        public void setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }
            this.source = source;
        }

        public void setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }
            this.target = target;
        }

        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.source.getClassLoader();
        }

        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(this.source);
        }

        public BeanCopier create() {
            Object key = KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BeanCopier)super.create(key);
        }

        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(this.source);
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(52, 1, this.getClassName(), BEAN_COPIER, null, "<generated>");
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(1, COPY, null);
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(this.source);
            PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(this.target);
            HashMap<String, PropertyDescriptor> names = new HashMap<String, PropertyDescriptor>();
            for (int i = 0; i < getters.length; ++i) {
                names.put(getters[i].getName(), getters[i]);
            }
            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            if (this.useConverter) {
                e.load_arg(1);
                e.checkcast(targetType);
                e.store_local(targetLocal);
                e.load_arg(0);
                e.checkcast(sourceType);
                e.store_local(sourceLocal);
            } else {
                e.load_arg(1);
                e.checkcast(targetType);
                e.load_arg(0);
                e.checkcast(sourceType);
            }
            for (int i = 0; i < setters.length; ++i) {
                PropertyDescriptor setter = setters[i];
                PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
                if (getter == null) continue;
                MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                if (this.useConverter) {
                    Type setterType = write.getSignature().getArgumentTypes()[0];
                    e.load_local(targetLocal);
                    e.load_arg(2);
                    e.load_local(sourceLocal);
                    e.invoke(read);
                    e.box(read.getSignature().getReturnType());
                    EmitUtils.load_class(e, setterType);
                    e.push(write.getSignature().getName());
                    e.invoke_interface(CONVERTER, CONVERT);
                    e.unbox_or_zero(setterType);
                    e.invoke(write);
                    continue;
                }
                if (!Generator.compatible(getter, setter)) continue;
                e.dup2();
                e.invoke(read);
                e.invoke(write);
            }
            e.return_value();
            e.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }
    }

    static interface BeanCopierKey {
        public Object newInstance(String var1, String var2, boolean var3);
    }
}

