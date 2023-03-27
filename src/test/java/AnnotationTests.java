import me.andrewc.Models.Model;
import org.junit.jupiter.api.Test;
import testModels.Book;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.UUID;

import me.andrewc.Annotations.Entity;
import me.andrewc.Annotations.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Annotations Tests
 */
public class AnnotationTests {
    @Test
    public void test_entity_annotation() {
        Class<Book> book = Book.class;
        Annotation[] annotations = book.getAnnotations();
        assertEquals(1, annotations.length);
    }

    @Test
    public void test_entity_annotation_value() {
        Class<Book> clazz = Book.class;
        Annotation[] annotations = clazz.getAnnotations();
        boolean found = false;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Entity) {
                Entity entity = (Entity) annotation;
                if ("books".equals(entity.tableName())) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found);
    }

    @Test
    public void test_id_annotation() {
        Field[] fields = Model.class.getDeclaredFields();
        int idFieldCount = 0;
        Field idField = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldCount++;
                idField = field;
            }
        }

        assertEquals(1, idFieldCount);

        Class<?> idFieldType = idField.getType();
        assertEquals(UUID.class, idFieldType);
    }

    @Test
    public void test_id_annotation_field_name() {
        Field[] fields = Model.class.getDeclaredFields();
        int idFieldCount = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldCount++;
                assertEquals(UUID.class, field.getType());

                // Check the field name
                assertEquals("id", field.getName().toLowerCase());
            }
        }
        assertEquals(1, idFieldCount);
    }


}
