package me.andrewc.Processors;

import me.andrewc.Annotations.Types.Decimal;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class DecimalProcessor {
    public static int scale;
    public static int precision;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DecimalProcessor.class);

    public static boolean process(Object object) throws InvalidValueLength, RuntimeException, InvalidTypeException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Decimal.class)) {
                // Get the value of the annotation (precision, scale)
                // Precision is the total number of digits
                String value = field.getAnnotation(Decimal.class).value();
                int ExpectedPrecision = Integer.parseInt(value.split(",")[0]);
                int ExpectedScale = Integer.parseInt(value.split(",")[1]);
                if (field.getType() == BigDecimal.class) {
                    try {
                        field.setAccessible(true);
                        // Get the value of the field
                        BigDecimal decimal = (BigDecimal) field.get(object);
                        // Now get the precision (length)
                        precision = decimal.precision();
                        // Now get the scale (number of digits after the decimal point)
                        scale = decimal.scale();
                        // Now check if the precision is greater than the maximum precision
                        if (precision > ExpectedPrecision) {
                            logger.warn("Precision is greater than defined");
                            throw new InvalidValueLength("Precision is greater than defined");
                        }
                        // Now check if the scale is greater than the maximum scale
                        if (scale > ExpectedScale) {
                            logger.warn("Scale is greater than defined");
                            throw new InvalidValueLength("Scale is greater than defined");
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("A significant error has occurred", e);
                        throw new RuntimeException("A significant error has occurred");
                    }
                } else {
                    logger.warn("Decimal annotation can only be used on BigDecimal fields");
                    throw new InvalidTypeException("Decimal annotation can only be used on BigDecimal fields");
                }
            }
        }
        return true;
    }
}
