package me.andrewc.Database;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The base class for all database objects
 * Connection is opened from here, and closed from here
 * Migrations can be run from here
 * The default jdbc is sqlite
 */
public class Base {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Base.class);
    private final Properties properties;
    private final Connection conn;

    public Base() throws SQLException, FileNotFoundException {
        properties = loadProperties(null);
        if (properties == null) {
            logger.error("Could not load properties file");
            throw new FileNotFoundException("Could not load properties file");
        }
        conn = createConnection();
        if (conn == null) {
            logger.error("Could not connect to database");
            throw new SQLException("Could not connect to database");
        }
    }

    public Base(String propertiesPath) throws FileNotFoundException, SQLException {
        properties = loadProperties(propertiesPath);
        if (properties == null) {
            logger.error("Could not load properties file: " + propertiesPath);
            throw new FileNotFoundException("Could not load properties file: " + propertiesPath);
        }
        conn = createConnection();
        if (conn == null) {
            logger.error("Could not connect to database");
            throw new SQLException("Could not connect to database");
        }
    }

    private @Nullable Properties loadProperties(String propertiesPath) {
        if (propertiesPath == null) {
            propertiesPath = "database.properties";
        }

        // Load properties from file
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesPath)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                properties = null;
            }
        } catch (IOException e) {
            logger.error("Could not load properties file", e);
        }
        return properties;
    }

    private @Nullable Connection createConnection() {
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username") == null ? "" : properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password") == null ? "" : properties.getProperty("jdbc.password");

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                logger.info("Connected to database");
                return conn;
            }
        } catch (SQLException e) {
            logger.error("Could not connect to database", e);
        }
        return null;
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean isConnected() {
        return conn != null;
    }

    public void close() {
        // Close connection to database
    }
    public void open() {
    }
}
