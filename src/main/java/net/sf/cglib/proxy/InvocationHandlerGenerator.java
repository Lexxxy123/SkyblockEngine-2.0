/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import java.util.List;
import net.sf.cglib.core.Block;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.proxy.CallbackGenerator;
import org.objectweb.asm.Type;

class InvocationHandlerGenerator
implements CallbackGenerator {
    public static final InvocationHandlerGenerator INSTANCE = new InvocationHandlerGenerator();
    private static final Type INVOCATION_HANDLER = TypeUtils.parseType("net.sf.cglib.proxy.InvocationHandler");
    private static final Type UNDECLARED_THROWABLE_EXCEPTION = TypeUtils.parseType("net.sf.cglib.proxy.UndeclaredThrowableException");
    private static final Type METHOD = TypeUtils.parseType("java.lang.reflect.Method");
    private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(Object, java.lang.reflect.Method, Object[])");

    InvocationHandlerGenerator() {
    }

    public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
        for (MethodInfo method : methods) {
            Signature impl = context.getImplSignature(method);
            ce.declare_field(26, impl.getName(), METHOD, null);
            CodeEmitter e = context.beginMethod(ce, method);
            Block handler = e.begin_block();
            context.emitCallback(e, context.getIndex(method));
            e.load_this();
            e.getfield(impl.getName());
            e.create_arg_array();
            e.invoke_interface(INVOCATION_HANDLER, INVOKE);
            e.unbox(method.getSignature().getReturnType());
            e.return_value();
            handler.end();
            EmitUtils.wrap_undeclared_throwable(e, handler, method.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
            e.end_method();
        }
    }

    public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {
        for (MethodInfo method : methods) {
            EmitUtils.load_method(e, method);
            e.putfield(context.getImplSignature(method).getName());
        }
    }
}

