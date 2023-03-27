import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import me.andrewc.Processors.DecimalProcessor;
import me.andrewc.Processors.TextProcessor;
import me.andrewc.Processors.VarcharProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testModels.BadBook;
import testModels.Book;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Field Types (Varchar, Text, Decimal)
 * Processors are also tested here
 */
public class ProcessorTests {
    private Book book;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("The Lord of the Rings");
        book.setDescription("A book about a ring");
        book.setPrice(new BigDecimal("19.99"));

        book2 = new Book();
        // Create a title 256 characters long
        for (int i = 0; i < 256; i++) {
            book2.setTitle(book2.getTitle() + "a");
        }
        // Same for description
        for (int i = 0; i < 1002; i++) {
            book2.setDescription(book2.getDescription() + "a");
        }
        // Same for price
        book2.setPrice(new BigDecimal("11111111111111.999"));
    }

    @Test
    public void test_varchar() throws InvalidValueLength, NoFieldFound, InvalidTypeException {
        boolean isValid = VarcharProcessor.process(book);

        assertTrue(isValid);
    }

    @Test
    public void test_invalid_varchar() {
        assertThrows(InvalidValueLength.class, () ->
                VarcharProcessor.process(book2));
    }

    @Test
    public void test_invalid_type_varchar() {
        assertThrows(InvalidTypeException.class, () ->
                VarcharProcessor.process(new BadBook(
                        17,
                        UUID.randomUUID(),
                        12
                )));
    }

    @Test
    public void test_text() throws NoFieldFound, InvalidValueLength, InvalidTypeException {
        boolean isValid = TextProcessor.process(book);

        assertTrue(isValid);
    }

    @Test
    public void test_invalid_text() {
        assertThrows(InvalidValueLength.class, () ->
                TextProcessor.process(book2));
    }

    @Test
    public void test_invalid_type_text() {
        assertThrows(InvalidTypeException.class, () ->
                TextProcessor.process(new BadBook(
                        17,
                        UUID.randomUUID(),
                        12
                ))
        );
    }

    @Test
    public void test_decimal() throws InvalidValueLength, InvalidTypeException {
        boolean isValid = DecimalProcessor.process(book);
        int scaleValue = book.getPrice().scale();
        int precisionValue = book.getPrice().precision();
        int processedScaleValue = DecimalProcessor.scale;
        int processedPrecisionValue = DecimalProcessor.precision;

        assertTrue(isValid);
        assertEquals(scaleValue, processedScaleValue);
        assertEquals(precisionValue, processedPrecisionValue);
    }

    @Test
    public void test_invalid_decimal() {
        assertThrows(InvalidValueLength.class, () ->
                DecimalProcessor.process(book2)
        );
    }

    @Test
    public void test_invalid_type_decimal() {
        assertThrows(InvalidTypeException.class, () ->
                DecimalProcessor.process(new BadBook(
                        17,
                        UUID.randomUUID(),
                        12
                ))
        );
    }

    @Test
    public void test_number() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_number() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_type_number() {
        // TODO: Implement
    }

    @Test
    public void test_date() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_date() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_type_date() {
        // TODO: Implement
    }

    @Test
    public void test_datetime() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_datetime() {
        // TODO: Implement
    }

    @Test
    public void test_invalid_type_datetime() {
        // TODO: Implement
    }
}
