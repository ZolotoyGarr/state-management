package javiki.course.serialization;

import com.fasterxml.jackson.databind.JavaType;

import java.util.*;

/**
 * Класс, представляющий тип состояния.
 */
public class StateType {
    private static final Map<String, StateType> REGISTERED_STATES = new HashMap<>();

    private final JavaType javaType;
    private final String name;
    private final String description;

    /**
     * Создает объект типа состояния.
     *
     * @param javaType    JavaType для состояния.
     * @param name        Имя состояния.
     * @param description Описание состояния.
     */
    public StateType(JavaType javaType, String name, String description) {
        this.javaType = javaType;
        this.name = name;
        this.description = description;
    }

    /**
     * Регистрирует новый тип состояния.
     *
     * @param state Тип состояния для регистрации.
     */
    public static void register(StateType state) {
        REGISTERED_STATES.put(state.getReadableName().toUpperCase(), state);
    }

    /**
     * Получает зарегистрированный тип состояния по имени.
     *
     * @param name Имя состояния.
     * @return Объект `StateType` или `null`, если не найден.
     */
    public static StateType get(String name) {
        return REGISTERED_STATES.get(name.toUpperCase());
    }

    /**
     * Получает `StateType` на основе имени папки.
     *
     * @param folderName Имя папки.
     * @return Соответствующий `StateType`.
     * @throws IllegalArgumentException Если имя состояния неизвестно.
     */
    public static StateType fromFolderName(String folderName) {
        for (StateType stateType : REGISTERED_STATES.values()) {
            if (stateType.getReadableName().equalsIgnoreCase(folderName)) {
                return stateType;
            }
        }
        throw new IllegalArgumentException("Unknown state name: " + folderName);
    }

    /**
     * Возвращает список всех зарегистрированных типов состояний.
     *
     * @return Список всех `StateType`.
     */
    public static List<StateType> getRegisteredStateTypes() {
        return new ArrayList<>(REGISTERED_STATES.values());
    }

    /**
     * Получает JavaType состояния.
     *
     * @return JavaType состояния.
     */
    public JavaType getJavaType() {
        return javaType;
    }

    /**
     * Получает читаемое имя состояния.
     *
     * @return Имя состояния.
     */
    public String getReadableName() {
        return name;
    }

    /**
     * Получает описание состояния.
     *
     * @return Описание состояния.
     */
    public String getDescription() {
        return description;
    }
}
