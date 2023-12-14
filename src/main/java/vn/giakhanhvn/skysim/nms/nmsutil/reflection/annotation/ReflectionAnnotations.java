package vn.giakhanhvn.skysim.nms.nmsutil.reflection.annotation;

import java.util.regex.Matcher;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.util.List;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.wrapper.MethodWrapper;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.MethodResolver;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.wrapper.FieldWrapper;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.FieldResolver;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.wrapper.ClassWrapper;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.minecraft.Minecraft;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.ClassResolver;
import java.util.regex.Pattern;

public class ReflectionAnnotations
{
    public static final ReflectionAnnotations INSTANCE;
    static final Pattern classRefPattern;
    
    private ReflectionAnnotations() {
    }
    
    public void load(final Object toLoad) {
        if (toLoad == null) {
            throw new IllegalArgumentException("toLoad cannot be null");
        }
        final ClassResolver classResolver = new ClassResolver();
        for (final java.lang.reflect.Field field : toLoad.getClass().getDeclaredFields()) {
            final Class classAnnotation = field.<Class>getAnnotation(Class.class);
            final Field fieldAnnotation = field.<Field>getAnnotation(Field.class);
            final Method methodAnnotation = field.<Method>getAnnotation(Method.class);
            if (classAnnotation != null || fieldAnnotation != null || methodAnnotation != null) {
                field.setAccessible(true);
                if (classAnnotation != null) {
                    final List<String> nameList = this.<Class>parseAnnotationVersions(Class.class, classAnnotation);
                    if (nameList.isEmpty()) {
                        throw new IllegalArgumentException("@Class names cannot be empty");
                    }
                    final String[] names = nameList.<String>toArray(new String[nameList.size()]);
                    for (int i = 0; i < names.length; ++i) {
                        names[i] = names[i].replace("{nms}", "net.minecraft.server." + Minecraft.VERSION.name()).replace("{obc}", "org.bukkit.craftbukkit." + Minecraft.VERSION.name());
                    }
                    try {
                        if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                            field.set(toLoad, classResolver.resolveWrapper(names));
                        }
                        else {
                            if (!java.lang.Class.class.isAssignableFrom(field.getType())) {
                                this.throwInvalidFieldType(field, toLoad, "Class or ClassWrapper");
                                return;
                            }
                            field.set(toLoad, classResolver.resolve(names));
                        }
                    }
                    catch (final ReflectiveOperationException e) {
                        if (!classAnnotation.ignoreExceptions()) {
                            this.throwReflectionException("@Class", field, toLoad, e);
                            return;
                        }
                    }
                }
                else if (fieldAnnotation != null) {
                    final List<String> nameList = this.<Field>parseAnnotationVersions(Field.class, fieldAnnotation);
                    if (nameList.isEmpty()) {
                        throw new IllegalArgumentException("@Field names cannot be empty");
                    }
                    final String[] names = nameList.<String>toArray(new String[nameList.size()]);
                    try {
                        final FieldResolver fieldResolver = new FieldResolver(this.<Field>parseClass(Field.class, fieldAnnotation, toLoad));
                        if (FieldWrapper.class.isAssignableFrom(field.getType())) {
                            field.set(toLoad, fieldResolver.resolveWrapper(names));
                        }
                        else {
                            if (!java.lang.reflect.Field.class.isAssignableFrom(field.getType())) {
                                this.throwInvalidFieldType(field, toLoad, "Field or FieldWrapper");
                                return;
                            }
                            field.set(toLoad, fieldResolver.resolve(names));
                        }
                    }
                    catch (final ReflectiveOperationException e) {
                        if (!fieldAnnotation.ignoreExceptions()) {
                            this.throwReflectionException("@Field", field, toLoad, e);
                            return;
                        }
                    }
                }
                else if (methodAnnotation != null) {
                    final List<String> nameList = this.<Method>parseAnnotationVersions(Method.class, methodAnnotation);
                    if (nameList.isEmpty()) {
                        throw new IllegalArgumentException("@Method names cannot be empty");
                    }
                    final String[] names = nameList.<String>toArray(new String[nameList.size()]);
                    final boolean isSignature = names[0].contains(" ");
                    for (final String s : names) {
                        if (s.contains(" ") != isSignature) {
                            throw new IllegalArgumentException("Inconsistent method names: Cannot have mixed signatures/names");
                        }
                    }
                    try {
                        final MethodResolver methodResolver = new MethodResolver(this.<Method>parseClass(Method.class, methodAnnotation, toLoad));
                        if (MethodWrapper.class.isAssignableFrom(field.getType())) {
                            if (isSignature) {
                                field.set(toLoad, methodResolver.resolveSignatureWrapper(names));
                            }
                            else {
                                field.set(toLoad, methodResolver.resolveWrapper(names));
                            }
                        }
                        else {
                            if (!java.lang.reflect.Method.class.isAssignableFrom(field.getType())) {
                                this.throwInvalidFieldType(field, toLoad, "Method or MethodWrapper");
                                return;
                            }
                            if (isSignature) {
                                field.set(toLoad, methodResolver.resolveSignature(names));
                            }
                            else {
                                field.set(toLoad, methodResolver.resolve(names));
                            }
                        }
                    }
                    catch (final ReflectiveOperationException e2) {
                        if (!methodAnnotation.ignoreExceptions()) {
                            this.throwReflectionException("@Method", field, toLoad, e2);
                            return;
                        }
                    }
                }
            }
        }
    }
    
     <A extends Annotation> List<String> parseAnnotationVersions(final java.lang.Class<A> clazz, final A annotation) {
        final List<String> list = new ArrayList<String>();
        try {
            final String[] names = (String[])clazz.getMethod("value", (java.lang.Class<?>[])new java.lang.Class[0]).invoke(annotation, new Object[0]);
            final Minecraft.Version[] versions = (Minecraft.Version[])clazz.getMethod("versions", (java.lang.Class<?>[])new java.lang.Class[0]).invoke(annotation, new Object[0]);
            if (versions.length == 0) {
                for (final String name : names) {
                    list.add(name);
                }
            }
            else {
                if (versions.length > names.length) {
                    throw new RuntimeException("versions array cannot have more elements than the names (" + clazz + ")");
                }
                for (int i = 0; i < versions.length; ++i) {
                    if (Minecraft.VERSION == versions[i]) {
                        list.add(names[i]);
                    }
                    else if (names[i].startsWith(">") && Minecraft.VERSION.newerThan(versions[i])) {
                        list.add(names[i].substring(1));
                    }
                    else if (names[i].startsWith("<") && Minecraft.VERSION.olderThan(versions[i])) {
                        list.add(names[i].substring(1));
                    }
                }
            }
        }
        catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
     <A extends Annotation> String parseClass(final java.lang.Class<A> clazz, final A annotation, final Object toLoad) {
        try {
            final String className = (String)clazz.getMethod("className", (java.lang.Class<?>[])new java.lang.Class[0]).invoke(annotation, new Object[0]);
            final Matcher matcher = ReflectionAnnotations.classRefPattern.matcher(className);
            while (matcher.find()) {
                if (matcher.groupCount() != 1) {
                    continue;
                }
                final String fieldName = matcher.group(1);
                final java.lang.reflect.Field field = toLoad.getClass().getField(fieldName);
                if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                    return ((ClassWrapper)field.get(toLoad)).getName();
                }
                if (java.lang.Class.class.isAssignableFrom(field.getType())) {
                    return ((java.lang.Class)field.get(toLoad)).getName();
                }
            }
            return className;
        }
        catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    void throwInvalidFieldType(final java.lang.reflect.Field field, final Object toLoad, final String expected) {
        throw new IllegalArgumentException("Field " + field.getName() + " in " + toLoad.getClass() + " is not of type " + expected + ", it's " + field.getType());
    }
    
    void throwReflectionException(final String annotation, final java.lang.reflect.Field field, final Object toLoad, final ReflectiveOperationException exception) {
        throw new RuntimeException("Failed to set " + annotation + " field " + field.getName() + " in " + toLoad.getClass(), exception);
    }
    
    static {
        INSTANCE = new ReflectionAnnotations();
        classRefPattern = Pattern.compile("@Class\\((.*)\\)");
    }
}
