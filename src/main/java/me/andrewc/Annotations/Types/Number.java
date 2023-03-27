package me.andrewc.Annotations.Types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.Integer;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Number {
    int max() default Integer.MAX_VALUE;
}


