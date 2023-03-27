package JDBCTests;

import me.andrewc.Database.Create;
import me.andrewc.Database.Insert;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;

import testModels.Book;

import static org.junit.jupiter.api.Assertions.*;

public class InsertTests {

    @BeforeEach
    public void setup() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        Create create = new Create("test.properties");
        create.CreateTable(Book.class);
    }

    @Test
    public void insert_single_success() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException, IllegalAccessException {
        Book book = new Book("The Hobbit", new BigDecimal("12.99"), "A book about a hobbit", 300);
        Insert insert = new Insert("test.properties");
        boolean success = insert.InsertSingle(Book.class, book);

        assertTrue(success);
    }
}
