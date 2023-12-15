package vn.giakhanhvn.skysim.nms.nmsutil.reflection.annotation;

import vn.giakhanhvn.skysim.nms.nmsutil.reflection.minecraft.Minecraft;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Class {
    String[] value();

    Minecraft.Version[] versions() default {};

    boolean ignoreExceptions() default true;
}
