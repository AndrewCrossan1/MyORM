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
import java.util.ArrayList;
import java.util.List;

import testModels.Book;

import static org.junit.jupiter.api.Assertions.*;

public class InsertTests {

    @BeforeEach
    public void setup() throws SQLException, FileNotFoundException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        Create create = new Create("test.properties");
        create.CreateTable(Book.class);
    }

    @Test
    public void insert_single_success() throws SQLException, FileNotFoundException {
        Book book = new Book("The Hobbit", new BigDecimal("12.99"), "A book about a hobbit", 300);
        Insert insert = new Insert("test.properties");
        boolean success = insert.InsertSingle(Book.class, book);

        assertTrue(success);
    }

    @Test
    public void insert_multiple_success() throws SQLException, FileNotFoundException {
        Book book = new Book("The Hobbit", new BigDecimal("12.99"), "A book about a hobbit", 300);
        Book book2 = new Book("The Hobbit 2", new BigDecimal("12.99"), "A book about a hobbit", 300);
        Book book3 = new Book("The Hobbit 3", new BigDecimal("12.99"), "A book about a hobbit", 300);
        Book book4 = new Book("The Hobbit 4", new BigDecimal("12.99"), "A book about a hobbit", 300);

        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        Insert insert = new Insert("test.properties");
        boolean success = insert.InsertMultiple(Book.class, books.toArray());
        assertTrue(success);
    }
}
