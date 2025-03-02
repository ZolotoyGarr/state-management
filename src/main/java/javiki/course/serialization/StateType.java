package javiki.course.serialization;

import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;

public class StateType {
    private static final Map<String, StateType> REGISTERED_STATES = new HashMap<>();

    private final JavaType javaType;
    private final String name;
    private final String description;

    public StateType(JavaType javaType, String name, String description) {
        this.javaType = javaType;
        this.name = name;
        this.description = description;
    }

    public static void register(StateType state) {
        REGISTERED_STATES.put(state.getReadableName().toUpperCase(), state);
    }

    public static StateType get(String name) {
        return REGISTERED_STATES.get(name.toUpperCase());
    }

    public static StateType fromFolderName(String folderName) {
        for (StateType stateType : REGISTERED_STATES.values()) {
            if (stateType.getReadableName().equalsIgnoreCase(folderName)) {
                return stateType;
            }
        }
        throw new IllegalArgumentException("Unknown state name: " + folderName);
    }

    public JavaType getJavaType() {
        return javaType;
    }

    public String getReadableName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
