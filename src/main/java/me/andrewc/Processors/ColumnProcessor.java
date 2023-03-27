package me.andrewc.Processors;

import me.andrewc.Annotations.Column;
import me.andrewc.Annotations.Types.Varchar;
import me.andrewc.Exceptions.InvalidTypeException;
import me.andrewc.Exceptions.InvalidValueLength;
import me.andrewc.Exceptions.NoFieldFound;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ColumnProcessor {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ColumnProcessor.class);
    private static ArrayList<String> columns;
    private static ArrayList<String> types;
    private static ArrayList<String> constraints;

    public static void process(Class<?> clazz) throws NoFieldFound, InvalidValueLength, InvalidTypeException {
        // First obtain ID Field (From Model class (Parent))
        Field[] parentField = clazz.getSuperclass().getDeclaredFields();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0 && parentField.length != 1) {
            logger.warn("No fields found in class " + clazz.getName());
            throw new NoFieldFound("No fields found in class");
        }
        columns = new ArrayList<>();
        types = new ArrayList<>();
        constraints = new ArrayList<>();
        columns.add(parentField[0].getName());
        types.add("VARCHAR(255)");
        constraints.add("PRIMARY KEY NOT NULL");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                columns.add(field.getName());
            }
            if (field.isAnnotationPresent(Varchar.class)) {
                VarcharProcessor.process(field);
                types.add("VARCHAR(" + field.getAnnotation(Varchar.class).length() + ")");
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Types.Number.class)) {
                NumberProcessor.process(field);
                types.add("INTEGER");
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Types.Date.class)) {
                DateProcessor.process(field);
                types.add("DATE");
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Types.DateTime.class)) {
                DateTimeProcessor.process(field);
                types.add("DATETIME");
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Types.Decimal.class)) {
                DecimalProcessor.process(field);
                String length = field.getAnnotation(me.andrewc.Annotations.Types.Decimal.class).value();
                if (length.equals("")) {
                    types.add("DECIMAL(10,2)");
                } else {
                    types.add("DECIMAL(" + length + ")");
                }
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Types.Text.class)) {
                TextProcessor.process(field);
                types.add("TEXT");
            }
            // Constraints
            if (field.isAnnotationPresent(me.andrewc.Annotations.Constraints.NotNull.class)) {
                constraints.add("NOT NULL");
            } else if (field.isAnnotationPresent(me.andrewc.Annotations.Constraints.PrimaryKey.class)) {
                constraints.add("PRIMARY KEY");
            } else {
                constraints.add("");
            }
        }
    }

    public static ArrayList<String> getColumns() {
        return columns;
    }
    public static ArrayList<String> getTypes() {
        return types;
    }
    public static ArrayList<String> getConstraints() {
        return constraints;
    }
}
