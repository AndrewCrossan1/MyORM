package me.andrewc.Database;

import me.andrewc.Processors.EntityProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Delete extends Base {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Delete.class);
    public Delete(String filePath) throws SQLException, FileNotFoundException {
        super(filePath);
    }

    public boolean Drop(Class<?> tableName, boolean Cascade) throws SQLException {
        Callable<Boolean> callable = () -> {
            Connection conn = getConnection();
            if (conn == null) {
                logger.error("Could not get connection");
                throw new SQLException("Could not get connection");
            }
            String query = "DROP TABLE IF EXISTS 1;";
            EntityProcessor.process(tableName);
            query = query.replace("1", EntityProcessor.getTableName());
            if (Cascade) {
                query = query.replace(";", " CASCADE;");
            }
            Statement stmt = conn.createStatement();
            stmt.execute(query);

            boolean deleted = false;
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, "main", EntityProcessor.getTableName(), null);
            if (tables.next()) {
                logger.info("Table " + EntityProcessor.getTableName() + " not dropped");
            } else {
                logger.warn("Table " + EntityProcessor.getTableName() + " dropped or does not exist");
                deleted = true;
            }
            stmt.close();
            conn.close();
            return deleted;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(callable).get();
        } catch (Exception ex) {
            logger.error("Failed to drop table: " + tableName.getName());
            return false;
        }
    }

    public boolean DeleteFrom(Class<?> table, String where, String condition) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            logger.error("Could not get connection");
            return false;
        }
        String query = "DELETE FROM 1 WHERE 2 = 3;";
        EntityProcessor.process(table);
        query = query.replace("1", EntityProcessor.getTableName());
        query = query.replace("2", where);
        query = query.replace("3", condition);

        Statement stmt = conn.createStatement();

        // Execute the query
        int result = stmt.executeUpdate(query);
        boolean deleted = false;

        if (result <= 0) {
            logger.error("Could not delete from table: " + EntityProcessor.getTableName());
        } else if (result == 1) {
            logger.info("Deleted 1 row from table: " + EntityProcessor.getTableName());
            deleted = true;
        } else {
            logger.info("Deleted " + result + " row(s) from table: " + EntityProcessor.getTableName());
            deleted = true;
        }
        // Close the connection
        stmt.close();
        conn.close();

        return deleted;
    }
}
