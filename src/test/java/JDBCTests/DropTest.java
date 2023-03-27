package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Database.Delete;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testModels.Book;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropTest {

    @BeforeEach
    public void setup() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        Create create = new Create("test.properties");
        create.CreateTable(Book.class);
    }

    @Test
    public void drop_success() throws SQLException, FileNotFoundException {
        Delete delete = new Delete("test.properties");
        boolean deleted = delete.Drop(Book.class, false);

        assertTrue(deleted);
    }

    @Test
    public void drop_failure() throws SQLException, FileNotFoundException {
        // Run twice to ensure that the table does not exist when we try to drop it
        Delete delete = new Delete("test.properties");
        delete.Drop(Book.class, false);

        assertThrows(SQLException.class, () -> {
            delete.Drop(Book.class, false);
        });
    }
}
