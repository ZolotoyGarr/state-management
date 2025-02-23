package javiki.course.serialization.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateManagerMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Map<String, Function<Object, Statable<?>>> MAPPERS = new HashMap<>();

    // ✅ Регистрация маппера для состояния
    public static void registerState(StateType stateType, Function<Object, Statable<?>> mapper) {
        MAPPERS.put(stateType.getReadableName().toUpperCase(), mapper);
    }
}
