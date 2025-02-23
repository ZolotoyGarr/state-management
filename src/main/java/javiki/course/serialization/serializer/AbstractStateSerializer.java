package javiki.course.serialization.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractStateSerializer implements StateSerializer {
    protected final String fileExtension;
    protected final ObjectMapper objectMapper;

    protected AbstractStateSerializer(String fileExtension, ObjectMapper objectMapper) {
        this.fileExtension = fileExtension;
        this.objectMapper = objectMapper;
    }

    @Override
    public final String getFileExtension() { // Делаем final, чтобы потомки не переопределяли
        return fileExtension;
    }

    protected String serialize(List<Statable<?>> statables) throws IOException {
        return objectMapper.writeValueAsString(statables.stream()
                .map(Statable::retrieveState)
                .collect(Collectors.toList()));
    }

    @Override
    public void serializeAndSave(Path filePath, List<Statable<?>> statables) throws IOException {
        Files.createDirectories(filePath.getParent()); // Создание директорий, если их нет

        String serializedValue = serialize(statables);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(serializedValue);
        }
    }

    public List<Statable<?>> downloadAndDeserialize(Path filePath, StateType stateName) throws IOException {
//        JavaType stringType = objectMapper.constructType(String.class);
//        JavaType listType = objectMapper.getTypeFactory()
//                .constructCollectionType(List.class, QuadrangleParameters.class);
//
//        JavaType mapType = objectMapper.getTypeFactory()
//                .constructMapType(Map.class, stringType, listType);
//
//        JavaType listOfMapsType = objectMapper.getTypeFactory()
//                .constructCollectionType(List.class, mapType);

        JavaType listOfStateType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, stateName.getJavaType());

        // Теперь десериализуем список объектов
        List<Object> objectList = objectMapper.readValue(
                Files.newBufferedReader(filePath), listOfStateType
        );

        Function<Object, Statable<?>> stateMapper = StateManagerMapper.MAPPERS.get(stateName.getReadableName());
        if (stateMapper == null) {
            throw new IllegalArgumentException("Ошибка: Не найден маппер для " + stateName.getReadableName());
        }

        return objectList.stream()
                .map(stateMapper)
                .collect(Collectors.toList());
    }
}
