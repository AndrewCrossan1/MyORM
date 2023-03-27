import org.junit.jupiter.api.Test;
import testModels.Book;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Model Tests
 */
public class ModelTests {
    @Test
    public void test_inheritance_ctor() {
        Book b1 = new Book();
        assertNotNull(b1.getID());
    }

    @Test
    public void test_book_toString() {
        Book b1 = new Book();
        assertEquals("Book" + " { ID: " + b1.getID() + " }", b1.toString());
    }

    @Test
    public void test_id_type() {
        Book b1 = new Book();
        assertEquals(UUID.class, b1.getID().getClass());
    }
}
