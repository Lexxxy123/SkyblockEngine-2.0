/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections;

import com.skyblock.skyblock.reflections.Store;
import com.skyblock.skyblock.reflections.util.ClasspathHelper;
import com.skyblock.skyblock.reflections.util.QueryFunction;
import com.skyblock.skyblock.reflections.util.ReflectionUtilsPredicates;
import com.skyblock.skyblock.reflections.util.UtilQueryBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ReflectionUtils
extends ReflectionUtilsPredicates {
    private static final List<String> objectMethodNames = Arrays.asList("equals", "hashCode", "toString", "wait", "notify", "notifyAll");
    public static final Predicate<Method> notObjectMethod = m2 -> !objectMethodNames.contains(m2.getName());
    public static final UtilQueryBuilder<Class<?>, Class<?>> SuperClass = element -> ctx -> {
        Class superclass = element.getSuperclass();
        return superclass != null && !superclass.equals(Object.class) ? Collections.singleton(superclass) : Collections.emptySet();
    };
    public static final UtilQueryBuilder<Class<?>, Class<?>> Interfaces = element -> ctx -> Stream.of(element.getInterfaces()).collect(Collectors.toCollection(LinkedHashSet::new));
    public static final UtilQueryBuilder<Class<?>, Class<?>> SuperTypes = new UtilQueryBuilder<Class<?>, Class<?>>(){

        @Override
        public QueryFunction<Store, Class<?>> get(Class<?> element) {
            return SuperClass.get(element).add(Interfaces.get(element));
        }

        @Override
        public QueryFunction<Store, Class<?>> of(Class<?> element) {
            return QueryFunction.single(element).getAll(SuperTypes::get);
        }
    };
    public static final UtilQueryBuilder<AnnotatedElement, Annotation> Annotations = new UtilQueryBuilder<AnnotatedElement, Annotation>(){

        @Override
        public QueryFunction<Store, Annotation> get(AnnotatedElement element) {
            return ctx -> Arrays.stream(element.getAnnotations()).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @Override
        public QueryFunction<Store, Annotation> of(AnnotatedElement element) {
            return ReflectionUtils.extendType().get(element).getAll(Annotations::get, Annotation::annotationType);
        }
    };
    public static final UtilQueryBuilder<AnnotatedElement, Class<? extends Annotation>> AnnotationTypes = new UtilQueryBuilder<AnnotatedElement, Class<? extends Annotation>>(){

        @Override
        public QueryFunction<Store, Class<? extends Annotation>> get(AnnotatedElement element) {
            return Annotations.get(element).map(Annotation::annotationType);
        }

        @Override
        public QueryFunction<Store, Class<? extends Annotation>> of(AnnotatedElement element) {
            return ReflectionUtils.extendType().get(element).getAll(AnnotationTypes::get, a2 -> a2);
        }
    };
    public static final UtilQueryBuilder<Class<?>, Method> Methods = element -> ctx -> Arrays.stream(element.getDeclaredMethods()).filter(notObjectMethod).collect(Collectors.toCollection(LinkedHashSet::new));
    public static final UtilQueryBuilder<Class<?>, Constructor> Constructors = element -> ctx -> Arrays.stream(element.getDeclaredConstructors()).collect(Collectors.toCollection(LinkedHashSet::new));
    public static final UtilQueryBuilder<Class<?>, Field> Fields = element -> ctx -> Arrays.stream(element.getDeclaredFields()).collect(Collectors.toCollection(LinkedHashSet::new));
    public static final UtilQueryBuilder<String, URL> Resources = element -> ctx -> new HashSet<URL>(ClasspathHelper.forResource(element, new ClassLoader[0]));

    public static <C, T> Set<T> get(QueryFunction<C, T> function) {
        return function.apply((Object)null);
    }

    public static <T> Set<T> get(QueryFunction<Store, T> queryFunction, Predicate<? super T> ... predicates) {
        return ReflectionUtils.get(queryFunction.filter(Arrays.stream(predicates).reduce(t2 -> true, Predicate::and)));
    }

    public static <T extends AnnotatedElement> UtilQueryBuilder<AnnotatedElement, T> extendType() {
        return element -> {
            if (element instanceof Class && !((Class)element).isAnnotation()) {
                QueryFunction<Store, Class> single = QueryFunction.single((Class)element);
                return single.add(single.getAll(SuperTypes::get));
            }
            return QueryFunction.single(element);
        };
    }

    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(T type, Predicate<Annotation> ... predicates) {
        return ReflectionUtils.get(Annotations.of(type), predicates);
    }

    public static Set<Class<?>> getAllSuperTypes(Class<?> type, Predicate<? super Class<?>> ... predicates) {
        Predicate[] predicateArray;
        if (predicates == null || predicates.length == 0) {
            Predicate[] predicateArray2 = new Predicate[1];
            predicateArray = predicateArray2;
            predicateArray2[0] = t2 -> !Object.class.equals(t2);
        } else {
            predicateArray = predicates;
        }
        Predicate[] filter = predicateArray;
        return ReflectionUtils.get(SuperTypes.of(type), filter);
    }

    public static Set<Class<?>> getSuperTypes(Class<?> type) {
        return ReflectionUtils.get(SuperTypes.get(type));
    }

    public static Set<Method> getAllMethods(Class<?> type, Predicate<? super Method> ... predicates) {
        return ReflectionUtils.get(Methods.of(type), predicates);
    }

    public static Set<Method> getMethods(Class<?> t2, Predicate<? super Method> ... predicates) {
        return ReflectionUtils.get(Methods.get(t2), predicates);
    }

    public static Set<Constructor> getAllConstructors(Class<?> type, Predicate<? super Constructor> ... predicates) {
        return ReflectionUtils.get(Constructors.of(type), predicates);
    }

    public static Set<Constructor> getConstructors(Class<?> t2, Predicate<? super Constructor> ... predicates) {
        return ReflectionUtils.get(Constructors.get(t2), predicates);
    }

    public static Set<Field> getAllFields(Class<?> type, Predicate<? super Field> ... predicates) {
        return ReflectionUtils.get(Fields.of(type), predicates);
    }

    public static Set<Field> getFields(Class<?> type, Predicate<? super Field> ... predicates) {
        return ReflectionUtils.get(Fields.get(type), predicates);
    }

    public static <T extends AnnotatedElement> Set<Annotation> getAnnotations(T type, Predicate<Annotation> ... predicates) {
        return ReflectionUtils.get(Annotations.get(type), predicates);
    }

    public static Map<String, Object> toMap(Annotation annotation) {
        return ReflectionUtils.get(Methods.of(annotation.annotationType()).filter(notObjectMethod.and(ReflectionUtils.withParametersCount(0)))).stream().collect(Collectors.toMap(Method::getName, (? super T m2) -> {
            List v1 = ReflectionUtils.invoke(m2, annotation, new Object[0]);
            return v1.getClass().isArray() && v1.getClass().getComponentType().isAnnotation() ? Stream.of((Annotation[])v1).map(ReflectionUtils::toMap).collect(Collectors.toList()) : v1;
        }));
    }

    public static Map<String, Object> toMap(Annotation annotation, AnnotatedElement element) {
        Map<String, Object> map = ReflectionUtils.toMap(annotation);
        if (element != null) {
            map.put("annotatedElement", element);
        }
        return map;
    }

    public static Annotation toAnnotation(Map<String, Object> map) {
        return ReflectionUtils.toAnnotation(map, (Class)map.get("annotationType"));
    }

    public static <T extends Annotation> T toAnnotation(Map<String, Object> map, Class<T> annotationType) {
        return (T)((Annotation)Proxy.newProxyInstance(annotationType.getClassLoader(), new Class[]{annotationType}, (proxy, method, args) -> notObjectMethod.test(method) ? map.get(method.getName()) : method.invoke(map, new Object[0])));
    }

    public static Object invoke(Method method, Object obj, Object ... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e2) {
            return e2;
        }
    }
}

