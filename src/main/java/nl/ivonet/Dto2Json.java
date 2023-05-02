package nl.ivonet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
            System.out.println("Please try again with better settings...");
            System.exit(1);
        }
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        final Class<?> clazz = systemClassLoader.loadClass(args[0]);
        generateDummyJSON(clazz);
    }

    public static void generateDummyJSON(final Class<?> clazz) throws IOException {
        final JsonNode jsonNode = mapper.readTree(printObj(clazz));
        final String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        System.out.println(prettyJson);
    }

    private static String printObj(final Class<?> clazz) {
        if (!visited.containsKey(clazz) || visited.get(clazz) <= 100) {
            visited.merge(clazz, 1, Integer::sum);
            final Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields.length == 0) {
                return name(clazz).toString();
            }
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
        } else if (UUID.class.equals(fieldType)) {
            return name(fieldType);
        } else if (Date.class.equals(fieldType)) {
            return name(fieldType);
        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else if (Character.class.equals(fieldType) || Character.TYPE.equals(fieldType)) {
            return name(fieldType);
        } else if (fieldType.isArray()) {
            final Class<?> componentType = fieldType.getComponentType();
            return String.format("[%s]", printObj(componentType));
        } else if (Instant.class.equals(fieldType)) {
            return "\"" + Instant.now().toString() + "\"";
        } else if (List.class.equals(fieldType)) {
            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] actualArgs;
            try {
                actualArgs = ((ParameterizedType) actualTypeArguments[0]).getActualTypeArguments();
            } catch (final Exception ignored) {
                actualArgs = actualTypeArguments;
            }
            final Class<?> clazz;
            try {
                clazz = ClassLoader.getSystemClassLoader().loadClass(actualArgs[0].getTypeName());
            } catch (final ClassNotFoundException ignored) {
                return "\"[Probably a Generic typed List]\"";
            }
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
