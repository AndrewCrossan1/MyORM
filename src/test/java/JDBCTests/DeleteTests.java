package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Database.Delete;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testModels.BadBook;
import testModels.Book;

import java.io.FileNotFoundException;
import java.sql.SQLException;

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
}
