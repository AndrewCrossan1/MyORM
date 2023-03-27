package me.andrewc.Annotations.Types;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Decimal annotation
 * Decimals should be defined with the following format: "10,2"
 * The first number is the precision and the second number is the scale
 * The datatype when declaring a field should be BigDecimal
 * @field value The value of the decimal (precision, scale)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decimal {
    String value() default "";
}
