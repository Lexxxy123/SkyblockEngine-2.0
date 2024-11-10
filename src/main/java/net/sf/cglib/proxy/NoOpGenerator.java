/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import java.util.List;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.proxy.CallbackGenerator;

class NoOpGenerator
implements CallbackGenerator {
    public static final NoOpGenerator INSTANCE = new NoOpGenerator();

    NoOpGenerator() {
    }

    public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
        for (MethodInfo method : methods) {
            if (!TypeUtils.isBridge(method.getModifiers()) && (!TypeUtils.isProtected(context.getOriginalModifiers(method)) || !TypeUtils.isPublic(method.getModifiers()))) continue;
            CodeEmitter e = EmitUtils.begin_method(ce, method);
            e.load_this();
            context.emitLoadArgsAndInvoke(e, method);
            e.return_value();
            e.end_method();
        }
    }

    public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {
    }
}

