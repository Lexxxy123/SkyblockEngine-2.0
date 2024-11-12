/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.scanners;

import com.skyblock.skyblock.reflections.Store;
import com.skyblock.skyblock.reflections.scanners.Scanner;
import com.skyblock.skyblock.reflections.util.FilterBuilder;
import com.skyblock.skyblock.reflections.util.JavassistHelper;
import com.skyblock.skyblock.reflections.util.NameHelper;
import com.skyblock.skyblock.reflections.util.QueryBuilder;
import com.skyblock.skyblock.reflections.util.QueryFunction;
import com.skyblock.skyblock.reflections.vfs.Vfs;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javassist.bytecode.ClassFile;

public enum Scanners implements Scanner,
QueryBuilder,
NameHelper
{
    SubTypes{
        {
            this.filterResultsBy(new FilterBuilder().excludePattern("java\\.lang\\.Object"));
        }

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            entries.add(this.entry(classFile.getSuperclass(), classFile.getName()));
            entries.addAll(this.entries(Arrays.asList(classFile.getInterfaces()), classFile.getName()));
        }
    }
    ,
    TypesAnnotated{

        @Override
        public boolean acceptResult(String annotation) {
            return super.acceptResult(annotation) || annotation.equals(Inherited.class.getName());
        }

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            entries.addAll(this.entries(JavassistHelper.getAnnotations(classFile::getAttribute), classFile.getName()));
        }
    }
    ,
    MethodsAnnotated{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getMethods(classFile).forEach(method -> entries.addAll(this.entries(JavassistHelper.getAnnotations(method::getAttribute), JavassistHelper.methodName(classFile, method))));
        }
    }
    ,
    ConstructorsAnnotated{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getConstructors(classFile).forEach(constructor -> entries.addAll(this.entries(JavassistHelper.getAnnotations(constructor::getAttribute), JavassistHelper.methodName(classFile, constructor))));
        }
    }
    ,
    FieldsAnnotated{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            classFile.getFields().forEach(field -> entries.addAll(this.entries(JavassistHelper.getAnnotations(field::getAttribute), JavassistHelper.fieldName(classFile, field))));
        }
    }
    ,
    Resources{

        @Override
        public boolean acceptsInput(String file) {
            return !file.endsWith(".class");
        }

        @Override
        public List<Map.Entry<String, String>> scan(Vfs.File file) {
            return Collections.singletonList(this.entry(file.getName(), file.getRelativePath()));
        }

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            throw new IllegalStateException();
        }

        @Override
        public QueryFunction<Store, String> with(String pattern) {
            return store -> store.getOrDefault(this.index(), Collections.emptyMap()).entrySet().stream().filter(entry -> ((String)entry.getKey()).matches(pattern)).flatMap(entry -> ((Set)entry.getValue()).stream()).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }
    ,
    MethodsParameter{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getMethods(classFile).forEach(method -> {
                String value = JavassistHelper.methodName(classFile, method);
                entries.addAll(this.entries(JavassistHelper.getParameters(method), value));
                JavassistHelper.getParametersAnnotations(method).forEach(annotations -> entries.addAll(this.entries((Collection<String>)annotations, value)));
            });
        }
    }
    ,
    ConstructorsParameter{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getConstructors(classFile).forEach(constructor -> {
                String value = JavassistHelper.methodName(classFile, constructor);
                entries.addAll(this.entries(JavassistHelper.getParameters(constructor), value));
                JavassistHelper.getParametersAnnotations(constructor).forEach(annotations -> entries.addAll(this.entries((Collection<String>)annotations, value)));
            });
        }
    }
    ,
    MethodsSignature{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getMethods(classFile).forEach(method -> entries.add(this.entry(JavassistHelper.getParameters(method).toString(), JavassistHelper.methodName(classFile, method))));
        }

        @Override
        public QueryFunction<Store, String> with(AnnotatedElement ... keys) {
            return QueryFunction.single(this.toNames(keys).toString()).getAll(this::get);
        }
    }
    ,
    ConstructorsSignature{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getConstructors(classFile).forEach(constructor -> entries.add(this.entry(JavassistHelper.getParameters(constructor).toString(), JavassistHelper.methodName(classFile, constructor))));
        }

        @Override
        public QueryFunction<Store, String> with(AnnotatedElement ... keys) {
            return QueryFunction.single(this.toNames(keys).toString()).getAll(this::get);
        }
    }
    ,
    MethodsReturn{

        @Override
        public void scan(ClassFile classFile, List<Map.Entry<String, String>> entries) {
            JavassistHelper.getMethods(classFile).forEach(method -> entries.add(this.entry(JavassistHelper.getReturnType(method), JavassistHelper.methodName(classFile, method))));
        }
    };

    private Predicate<String> resultFilter = s2 -> true;

    @Override
    public String index() {
        return this.name();
    }

    public Scanners filterResultsBy(Predicate<String> filter) {
        this.resultFilter = filter;
        return this;
    }

    @Override
    public final List<Map.Entry<String, String>> scan(ClassFile classFile) {
        ArrayList<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>();
        this.scan(classFile, entries);
        return entries.stream().filter(a2 -> this.acceptResult((String)a2.getKey())).collect(Collectors.toList());
    }

    abstract void scan(ClassFile var1, List<Map.Entry<String, String>> var2);

    protected boolean acceptResult(String fqn) {
        return fqn != null && this.resultFilter.test(fqn);
    }
}

