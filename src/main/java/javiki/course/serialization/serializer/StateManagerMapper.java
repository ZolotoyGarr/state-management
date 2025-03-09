package javiki.course.serialization.serializer;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;
import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateManagerMapper {
    private static final Map<String, Function<Object, Statable<?>>> MAPPERS = new HashMap<>();

    /**
     * ✅ Регистрирует новое состояние и его маппер
     */
    public static void registerState(String stateName, JavaType javaType, Function<Object, Statable<?>> mapper) {
        if (stateName == null || javaType == null || mapper == null) {
            throw new IllegalArgumentException("❌ Ошибка: Попытка зарегистрировать null маппер!");
        }

        if (StateType.get(stateName) == null) {
            StateType.register(new StateType(javaType, stateName, "Динамически зарегистрированное состояние"));
        }

        MAPPERS.put(stateName, mapper);
    }

    /**
     * ✅ Проверяет, зарегистрирован ли маппер для данного состояния
     */
    public static boolean hasMapper(String stateName) {
        return MAPPERS.containsKey(stateName);
    }

    /**
     * ✅ Получает маппер по `StateType`, если он зарегистрирован.
     */
    public static Function<Object, Statable<?>> getMapper(StateType stateType) {
        Function<Object, Statable<?>> mapper = MAPPERS.get(stateType.getReadableName());
        if (mapper == null) {
            throw new IllegalArgumentException("❌ Ошибка: Не найден маппер для " + stateType.getReadableName());
        }
        return mapper;
    }
}
