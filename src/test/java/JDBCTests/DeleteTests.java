package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Database.Delete;
import me.andrewc.Database.Insert;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testModels.BadBook;
import testModels.Book;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteTests {

    @BeforeEach
    public void setup() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        Create create = new Create("test.properties");
        create.CreateTable(Book.class);
    }

    @Test
    public void test_table_drop_success() throws SQLException, FileNotFoundException {
        Delete delete = new Delete("test.properties");
        boolean result = delete.Drop(Book.class, false);

        assertTrue(result);
    }

    @Test
    public void test_table_drop_failure() throws SQLException, FileNotFoundException {
        Delete delete = new Delete("test.properties");
        boolean result = delete.Drop(BadBook.class, false);

        assertTrue(result);
    }

    @Test
    public void test_delete_from_success() throws SQLException, FileNotFoundException {
        Book book = new Book("The Hobbit", new BigDecimal("19.99"), "Book bout hobbit", 1937);

        Insert insert = new Insert("test.properties");
        insert.InsertSingle(Book.class, book);

        Delete delete = new Delete("test.properties");
        boolean result = delete.DeleteFrom(Book.class, "title", "The Hobbit");

        assertTrue(result);
    }

    @Test
    public void test_delete_from_failure() throws SQLException, FileNotFoundException {
        Book book = new Book("The Hobbit", new BigDecimal("19.99"), "Book bout hobbit", 1937);

        Insert insert = new Insert("test.properties");
        insert.InsertSingle(Book.class, book);

        Delete delete = new Delete("test.properties");
        boolean result = delete.DeleteFrom(Book.class, "title", "The Hobbit 7");

        assertFalse(result);
    }
}
