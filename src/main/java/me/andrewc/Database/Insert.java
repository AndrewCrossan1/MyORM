package me.andrewc.Database;

import me.andrewc.Processors.ColumnProcessor;
import me.andrewc.Processors.EntityProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Insert extends Base {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Insert.class);

    public Insert(String filePath) throws SQLException, FileNotFoundException {
        super(filePath);
    }

    /**
     * Insert a new row into the table
     */
    public <T> boolean InsertSingle(Class<?> table, T instance) {
        // Run all of this in a new thread
        Callable<Boolean> result = () -> {
            try {
                EntityProcessor.process(table);
                Connection conn = getConnection();

                // Get the column names and values from the table (Column name is Key, Value is V)
                ColumnProcessor.process(table);
                HashMap<String, Object> columnKV = new HashMap<>();

                for (Field field : instance.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    columnKV.put(field.getName(), field.get(instance));
                }
                // If the ID field is not already in the columnKV map, add it manually
                if (!columnKV.containsKey("ID")) {
                    Field idField;
                    try {
                        idField = instance.getClass().getSuperclass().getDeclaredField("ID");
                        idField.setAccessible(true);
                        columnKV.put("ID", idField.get(instance).toString());
                    } catch (NoSuchFieldException ex) {
                        logger.error("Failed to find ID field in class: " + instance.getClass().getName());
                        return false;
                    }
                }

                // Formulate query
                String query = "INSERT INTO " + EntityProcessor.getTableName() + " (2) VALUES (3)";
                query = query.replace("2", String.join(", ", columnKV.keySet()));

                // Add values
                StringBuilder values = new StringBuilder();
                for (Object value : columnKV.values()) {
                    if (value instanceof String) {
                        values.append("'").append(value).append("', ");
                    } else {
                        values.append(value).append(", ");
                    }
                }
                values = new StringBuilder(values.substring(0, values.length() - 2));
                query = query.replace("3", values.toString());

                // Execute query
                Statement stmt = conn.createStatement();
                int result1 = stmt.executeUpdate(query);

                if (result1 <= 0) {
                    logger.error("Failed to insert row into table: " + EntityProcessor.getTableName());
                    return false;
                } else {
                    logger.info("Successfully inserted row into table: " + EntityProcessor.getTableName());
                }
                return true;
            } catch (Exception e) {
                logger.error("Failed to insert row into table: " + EntityProcessor.getTableName());
            }
            return false;
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> task = executor.submit(result);

        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Insert multiple rows into the table
     */
    public <T> boolean InsertMultiple(Class<?> table, T[] instances) {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
            logger.error("Failed to clear console");
        }
        // Split the instances into chunks of 10
        int chunkSize = 10;
        int numOfChunks = (int) Math.ceil((double) instances.length / chunkSize);

        // Create a 2D Array where each index is a sub array of the original instances array
        Object[][] chunks = new Object[numOfChunks][];
        for (int i = 0; i < numOfChunks; ++i) {
            int start = i * chunkSize;
            int length = Math.min(instances.length - start, chunkSize);
            Object[] temp = new Object[length];
            System.arraycopy(instances, start, temp, 0, length);
            chunks[i] = temp;
        }

        // Add remaining instances to final chunk if necessary
        if (instances.length % chunkSize != 0) {
            int finalChunkIndex = numOfChunks - 1;
            int finalChunkSize = instances.length % chunkSize;
            Object[] finalChunk = new Object[finalChunkSize];
            System.arraycopy(instances, instances.length - finalChunkSize, finalChunk, 0, finalChunkSize);
            chunks[finalChunkIndex] = finalChunk;
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (Object[] chunk : chunks) {
            tasks.add(() -> {
                for (Object instance : chunk) {
                    if (!InsertSingle(table, instance)) {
                        return false;
                    }
                }
                return true;
            });
        }

        try {
            List<Future<Boolean>> results = executor.invokeAll(tasks);

            for (Future<Boolean> result : results) {
                if (!result.get()) {
                    executor.shutdownNow();
                    return false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            executor.shutdownNow();
            return false;
        } finally {
            executor.shutdown();
        }
        return true;
    }
}
