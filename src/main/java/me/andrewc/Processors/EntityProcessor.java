package me.andrewc.Processors;

import me.andrewc.Annotations.Entity;
import org.slf4j.Logger;

/**
 * Processor for Entity annotation
 * Retrieves table name
 */
public class EntityProcessor {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EntityProcessor.class);

    private static String tableName;

    public static void process(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            tableName = entity.tableName();
            logger.info("Entity found: " + tableName);
        } else {
            // Inform user that class is not an entity
            logger.warn("Class " + clazz.getName() + " is not an entity");
            tableName = null;
        }
    }

    public static String getTableName() {
        return tableName;
    }
}

