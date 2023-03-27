package me.andrewc.Processors;

import me.andrewc.Annotations.Types.Varchar;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class VarcharProcessor {
    private static int max = 0;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(VarcharProcessor.class);

    public static boolean process(Object obj) throws InvalidValueLength, InvalidTypeException, NoFieldFound {
        Class<?> boonk = obj.getClass();
        Field[] fields = boonk.getDeclaredFields();
        if (fields.length == 0) {
            logger.warn("No fields found in class " + boonk.getName());
            throw new NoFieldFound("No fields found in class " + boonk.getName());
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Varchar.class)) {
                max = field.getAnnotation(Varchar.class).length();
                logger.info("Max length of " + field.getName() + " is " + max);
                if (field.getType() == String.class) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(obj);
                        if (value != null && value.length() > max) {
                            logger.warn("Value length is greater than the maximum length of " + max);
                            throw new InvalidValueLength("Value length is greater than the maximum length of " + max);
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("A significant error has occurred", e);
                        throw new RuntimeException("A significant error has occurred");
                    }
                } else {
                    logger.warn("Varchar annotation can only be used on String fields");
                    throw new InvalidTypeException("Varchar annotation can only be used on String fields");
                }
            }
        }
        return true;
    }

    public static int getMax() {
        return max;
    }
}
