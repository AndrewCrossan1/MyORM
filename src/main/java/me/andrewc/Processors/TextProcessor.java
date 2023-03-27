package me.andrewc.Processors;

import me.andrewc.Annotations.Types.Text;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class TextProcessor {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TextProcessor.class);

    public static boolean process(Object object) throws InvalidTypeException, NoFieldFound, InvalidValueLength {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            logger.warn("No fields found in class " + clazz.getName());
            throw new NoFieldFound("No fields found in class " + clazz.getName());
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Text.class)) {
                int maxLength = field.getAnnotation(Text.class).length();
                if (field.getType() == String.class) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(object);
                        if (value != null && value.length() > maxLength) {
                            logger.warn("Value length is greater than the maximum length of " + maxLength);
                            throw new InvalidValueLength("Value length is greater than the maximum length of " + maxLength);
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("A significant error has occurred", e);
                        throw new RuntimeException("A significant error has occurred");
                    }
                } else {
                    logger.warn("Text annotation can only be used on String fields");
                    throw new InvalidTypeException("Text annotation can only be used on String fields");
                }
            }
        }
        return true;
    }
}
