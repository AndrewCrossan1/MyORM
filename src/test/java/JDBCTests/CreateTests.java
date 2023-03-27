package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.junit.jupiter.api.Test;
import testModels.Book;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTests {
    @Test
    public void test_table_creation() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        Create create = new Create("test.properties");
        boolean created = create.CreateTable(Book.class);

        assertTrue(created);
    }
}
