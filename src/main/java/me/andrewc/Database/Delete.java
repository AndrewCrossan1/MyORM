package me.andrewc.Database;

import me.andrewc.Processors.EntityProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.sql.*;

public class Delete extends Base {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Delete.class);
    public Delete(String filePath) throws SQLException, FileNotFoundException {
        super(filePath);
    }

    public boolean Drop(Class<?> tableName, boolean Cascade) throws SQLException {
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

        logger.info("Dropping table: " + tableName);

        boolean deleted = false;
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, "main", EntityProcessor.getTableName(), null);
        if (tables.next()) {
            logger.info("Table " + EntityProcessor.getTableName() + " not dropped");
        } else {
            logger.warn("Table " + EntityProcessor.getTableName() + " dropped");
            deleted = true;
        }
        stmt.close();
        conn.close();
        return deleted;
    }

    public boolean DeleteFrom(String tableName, String where) {
        logger.info("Deleting from table: " + tableName + " where: " + where);
        return true;
    }
}
