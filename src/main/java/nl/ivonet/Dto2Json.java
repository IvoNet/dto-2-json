package nl.ivonet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sample utility for generating dummy JSON sample code from a JAva class directly.
 */
public class Dto2Json {

    /**
     * Used to avoid infinite loops.
     */
    private static final Map<Class<?>, Integer> visited = new HashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(final String[] args) throws IOException, ClassNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java Dto2Json <class> [output file]");
            System.exit(1);
        }
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        final Class<?> clazz = systemClassLoader.loadClass(args[0]);
        generateDummyJSON(clazz);
    }

    public static void generateDummyJSON(final Class<?> clazz) throws IOException {
        final StringWriter out = new StringWriter();
        try (final PrintWriter writer = new PrintWriter(out)) {
            writer.println(printObj(clazz));
            final JsonNode jsonNode = mapper.readTree(out.toString());
            final String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            System.out.println(prettyJson);
        }
    }

    private static String printObj(final Class<?> clazz) {
        if (!visited.containsKey(clazz) || visited.get(clazz) <= 100) {
            visited.merge(clazz, 1, Integer::sum);
            final Field[] declaredFields = clazz.getDeclaredFields();
            return "{" +
                    Stream.of(declaredFields)
                            .filter(e -> !Modifier.isStatic(e.getModifiers()))
                            .map(field -> String.format("  \"%s\" : %s%n", field.getName(), printFieldValue(field)))
                            .collect(Collectors.joining(String.format(",%n"))) +
                    "}";
        }
        return "";
    }

    private static Object printFieldValue(final Field field) {
        final Class<?> fieldType = field.getType();
        if (String.class.equals(fieldType)) {
            return name(fieldType);
        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return printEnum(fieldType);
        } else if (Double.class.equals(fieldType)) {
            return name(fieldType);
        } else if (LocalDate.class.equals(fieldType)) {
            return name(fieldType);
        } else if (LocalDateTime.class.equals(fieldType)) {
            return name(fieldType);
        } else if (OffsetDateTime.class.equals(fieldType)) {
            return name(fieldType);
        } else if (Date.class.equals(fieldType)) {
            return name(fieldType);
        } else if (List.class.equals(fieldType)) {
            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final Class<?> clazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return String.format("[%s]", printObj(clazz));
        } else if (fieldType.isAssignableFrom(Number.class)) {
            return name(fieldType);
        } else if (BigDecimal.class.equals(fieldType)) {
            return name(fieldType);
        } else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else {
            return printObj(fieldType);
        }
    }

    private static String printEnum(final Class<?> fieldType) {
        final Field[] declaredFields = fieldType.getDeclaredFields();
        return "\"[" + Arrays.stream(declaredFields)
                .filter(field -> field.getType() == fieldType)
                .map(Field::getName)
                .collect(Collectors.joining("|")) +
                "]\"";
    }

    private static Object name(final Class<?> fieldType) {
        return "\"" + fieldType.getSimpleName() + "\"";
    }
}
