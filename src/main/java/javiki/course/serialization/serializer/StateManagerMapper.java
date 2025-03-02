package javiki.course.serialization.serializer;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateManagerMapper {
    public static final Map<String, Function<Object, Statable<?>>> MAPPERS = new HashMap<>();

    public static void registerState(StateType stateType, Function<Object, Statable<?>> mapper) {
        if (stateType == null || mapper == null) {
            throw new IllegalArgumentException("❌ Ошибка: Попытка зарегистрировать null маппер!");
        }
        MAPPERS.put(stateType.getReadableName(), mapper);
    }

    public static Function<Object, Statable<?>> getMapper(StateType stateType) {
        return MAPPERS.get(stateType.getReadableName());
    }
}
