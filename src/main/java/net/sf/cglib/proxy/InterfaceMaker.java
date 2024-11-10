/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public class InterfaceMaker
extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(InterfaceMaker.class.getName());
    private Map signatures = new HashMap();

    public InterfaceMaker() {
        super(SOURCE);
    }

    public void add(Signature sig, Type[] exceptions) {
        this.signatures.put(sig, exceptions);
    }

    public void add(Method method) {
        this.add(ReflectUtils.getSignature(method), ReflectUtils.getExceptionTypes(method));
    }

    public void add(Class clazz) {
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            if (m.getDeclaringClass().getName().equals("java.lang.Object")) continue;
            this.add(m);
        }
    }

    public Class create() {
        this.setUseCache(false);
        return (Class)super.create(this);
    }

    protected ClassLoader getDefaultClassLoader() {
        return null;
    }

    protected Object firstInstance(Class type) {
        return type;
    }

    protected Object nextInstance(Object instance) {
        throw new IllegalStateException("InterfaceMaker does not cache");
    }

    public void generateClass(ClassVisitor v) throws Exception {
        ClassEmitter ce = new ClassEmitter(v);
        ce.begin_class(52, 1537, this.getClassName(), null, null, "<generated>");
        for (Signature sig : this.signatures.keySet()) {
            Type[] exceptions = (Type[])this.signatures.get(sig);
            ce.begin_method(1025, sig, exceptions).end_method();
        }
        ce.end_class();
    }
}

