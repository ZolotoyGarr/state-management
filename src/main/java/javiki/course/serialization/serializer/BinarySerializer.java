package javiki.course.serialization.serializer;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BinarySerializer implements StateSerializer, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void serializeAndSave(Path filePath, List<Statable<?>> statables) throws IOException {
        Files.createDirectories(filePath.getParent()); // Создаем директории, если их нет

        List<Object> stateObjects = new ArrayList<>();
        for (Statable<?> statable : statables) {
            stateObjects.add(statable.retrieveState());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(stateObjects);
            System.out.println("✅ Бинарная сериализация сохранена в " + filePath);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Statable<?>> downloadAndDeserialize(Path filePath, StateType stateName) throws IOException {
        List<?> stateResult;

        System.out.println("📥 Начинаем загрузку бинарного состояния из: " + filePath);

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            stateResult = (List<?>) ois.readObject();
            System.out.println("✅ Бинарный файл успешно прочитан.");
        } catch (ClassNotFoundException e) {
            throw new IOException("❌ Ошибка при десериализации: класс не найден", e);
        } catch (IOException e) {
            throw new IOException("❌ Ошибка при чтении бинарного файла: " + filePath, e);
        }

        Function<Object, Statable<?>> stateMapper = StateManagerMapper.MAPPERS.get(stateName.getReadableName());
        if (stateMapper == null) {
            throw new IllegalArgumentException("❌ Ошибка: Не найден маппер для " + stateName.getReadableName());
        }

        List<Statable<?>> deserializedObjects = stateResult.stream()
                .map(stateMapper)
                .collect(Collectors.toList());

        System.out.println("✅ Десериализовано объектов: " + deserializedObjects.size());
        return deserializedObjects;
    }

    @Override
    public String getFileExtension() {
        return ".bin";
    }
}
