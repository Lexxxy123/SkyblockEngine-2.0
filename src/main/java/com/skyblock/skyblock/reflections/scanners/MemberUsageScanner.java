/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.scanners;

import com.skyblock.skyblock.reflections.ReflectionsException;
import com.skyblock.skyblock.reflections.scanners.Scanner;
import com.skyblock.skyblock.reflections.util.ClasspathHelper;
import com.skyblock.skyblock.reflections.util.JavassistHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import javax.annotation.Nonnull;

public class MemberUsageScanner
implements Scanner {
    private Predicate<String> resultFilter = s2 -> true;
    private final ClassLoader[] classLoaders;
    private volatile ClassPool classPool;

    public MemberUsageScanner() {
        this(ClasspathHelper.classLoaders(new ClassLoader[0]));
    }

    public MemberUsageScanner(@Nonnull ClassLoader[] classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    public List<Map.Entry<String, String>> scan(ClassFile classFile) {
        ArrayList<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>();
        CtClass ctClass = null;
        try {
            ctClass = this.getClassPool().get(classFile.getName());
            for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
                this.scanMember(ctConstructor, entries);
            }
            for (CtBehavior ctBehavior : ctClass.getDeclaredMethods()) {
                this.scanMember(ctBehavior, entries);
            }
        } catch (Exception e2) {
            throw new ReflectionsException("Could not scan method usage for " + classFile.getName(), e2);
        } finally {
            if (ctClass != null) {
                ctClass.detach();
            }
        }
        return entries;
    }

    public Scanner filterResultsBy(Predicate<String> filter) {
        this.resultFilter = filter;
        return this;
    }

    private void scanMember(CtBehavior member, final List<Map.Entry<String, String>> entries) throws CannotCompileException {
        final String key = member.getDeclaringClass().getName() + "." + member.getMethodInfo().getName() + "(" + MemberUsageScanner.parameterNames(member.getMethodInfo()) + ")";
        member.instrument(new ExprEditor(){

            @Override
            public void edit(NewExpr e2) {
                try {
                    MemberUsageScanner.this.add(entries, e2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.parameterNames(e2.getConstructor().getMethodInfo()) + ")", key + " #" + e2.getLineNumber());
                } catch (NotFoundException e1) {
                    throw new ReflectionsException("Could not find new instance usage in " + key, e1);
                }
            }

            @Override
            public void edit(MethodCall m2) {
                try {
                    MemberUsageScanner.this.add(entries, m2.getMethod().getDeclaringClass().getName() + "." + m2.getMethodName() + "(" + MemberUsageScanner.parameterNames(m2.getMethod().getMethodInfo()) + ")", key + " #" + m2.getLineNumber());
                } catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + m2.getClassName() + " in " + key, e2);
                }
            }

            @Override
            public void edit(ConstructorCall c2) {
                try {
                    MemberUsageScanner.this.add(entries, c2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.parameterNames(c2.getConstructor().getMethodInfo()) + ")", key + " #" + c2.getLineNumber());
                } catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + c2.getClassName() + " in " + key, e2);
                }
            }

            @Override
            public void edit(FieldAccess f2) {
                try {
                    MemberUsageScanner.this.add(entries, f2.getField().getDeclaringClass().getName() + "." + f2.getFieldName(), key + " #" + f2.getLineNumber());
                } catch (NotFoundException e2) {
                    throw new ReflectionsException("Could not find member " + f2.getFieldName() + " in " + key, e2);
                }
            }
        });
    }

    private void add(List<Map.Entry<String, String>> entries, String key, String value) {
        if (this.resultFilter.test(key)) {
            entries.add(this.entry(key, value));
        }
    }

    public static String parameterNames(MethodInfo info) {
        return String.join((CharSequence)", ", JavassistHelper.getParameters(info));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ClassPool getClassPool() {
        if (this.classPool == null) {
            MemberUsageScanner memberUsageScanner = this;
            synchronized (memberUsageScanner) {
                if (this.classPool == null) {
                    this.classPool = new ClassPool();
                    for (ClassLoader classLoader : this.classLoaders) {
                        this.classPool.appendClassPath(new LoaderClassPath(classLoader));
                    }
                }
            }
        }
        return this.classPool;
    }
}

