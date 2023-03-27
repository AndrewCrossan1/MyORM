package me.andrewc.Database;


import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import me.andrewc.Processors.EntityProcessor;
import me.andrewc.Processors.ColumnProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class Create extends Base {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Create.class);

    public Create(String filePath) throws SQLException, FileNotFoundException {
        super(filePath);
    }

    public boolean CreateTable(Class<?> clazz) throws SQLException, NoFieldFound, InvalidValueLength, InvalidTypeException {
        // Get connection
        Connection conn = getConnection();
        if (conn == null) {
            logger.error("Could not get connection");
            throw new SQLException("Could not get connection");
        }
        // Create Statement
        String query = "CREATE TABLE IF NOT EXISTS 1 (2);";
        // Replace 1 with table name
        EntityProcessor.process(clazz);
        query = query.replace("1", EntityProcessor.getTableName());
        // Now insert column names
        ColumnProcessor.process(clazz);
        ArrayList<String> columns = ColumnProcessor.getColumns();
        // Now get the column types
        ArrayList<String> types = ColumnProcessor.getTypes();
        // Execute Statement
        ArrayList<String> constraints = ColumnProcessor.getConstraints();

        // Construct each column with its type and constraints
        ArrayList<String> lines = new ArrayList<>();

        StringBuilder line = new StringBuilder();
        for (int x = 0; x < columns.size(); x++) {
            line.append(columns.get(x));
            line.append(" ");
            line.append(types.get(x));
            if (constraints.size() > 0) {
                line.append(" ");
                line.append(constraints.get(x));
            }
            lines.add(line.toString());
            line = new StringBuilder();
        }

        // Now construct the query, (replace 2 with the generated lines)
        query = query.replace("2", String.join(", ", lines));

        PreparedStatement stmt = conn.prepareStatement(query);

        logger.info("Connection? " + (conn == null ? "No" : "Yes"));
        logger.info("Query: " + query);

        // Execute the query
        stmt.execute();

        // Check if table was created
        boolean created = false;
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, "main", EntityProcessor.getTableName(), null);
        if (tables.next()) {
            logger.info("Table " + EntityProcessor.getTableName() + " created");
            created = true;
        } else {
            logger.info("Table " + EntityProcessor.getTableName() + " not created");
        }

        // Print the location of the sqlite database file
        logger.info("Database file location: " + conn.getMetaData().getURL());
        // Close connection
        conn.close();
        return created;
    }
}
