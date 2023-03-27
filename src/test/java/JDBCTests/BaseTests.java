package JDBCTests;

import me.andrewc.Database.Base;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTests {
    @Test
    public void test_success() throws SQLException, FileNotFoundException {
        Base base = new Base();
        assertNotNull(base.getConnection());
        assertTrue(base.isConnected());
    }

    @Test
    public void test_success_property_param() throws SQLException, FileNotFoundException {
        Base base = new Base("test.properties");
        assertNotNull(base.getConnection());
        assertTrue(base.isConnected());
    }

    @Test
    public void test_failure() {
        assertThrows(FileNotFoundException.class, () -> {
            new Base("nonexistent.properties");
        });
    }
}
