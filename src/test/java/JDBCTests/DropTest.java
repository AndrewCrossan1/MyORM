package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Database.Delete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testModels.Book;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DropTest {

    @BeforeEach
    public void setup() throws SQLException, FileNotFoundException {
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

        assertFalse(delete.Drop(Book.class, false));
    }
}
