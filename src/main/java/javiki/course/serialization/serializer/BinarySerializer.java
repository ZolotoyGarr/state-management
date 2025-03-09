package javiki.course.serialization.serializer;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BinarySerializer {

    public void serializeAndSave(Path filePath, List<Statable<?>> statables) throws IOException {
        Files.createDirectories(filePath.getParent()); // Создание директорий, если их нет
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(statables);
        }
        System.out.println("✅ Бинарная сериализация сохранена в " + filePath);
    }

    public List<Statable<?>> downloadAndDeserialize(Path filePath, StateType stateType) throws IOException {
        System.out.println("📥 Начинаем загрузку бинарного состояния из: " + filePath);

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            List<?> objectList = (List<?>) ois.readObject();
            System.out.println("✅ Бинарный файл успешно прочитан.");

            // 🛠 Преобразуем в нужный тип `Statable`
            Function<Object, Statable<?>> stateMapper = StateManagerMapper.getMapper(stateType);
            if (stateMapper == null) {
                throw new IllegalArgumentException("Ошибка: Не найден маппер для " + stateType.getReadableName());
            }

            return objectList.stream()
                    .map(stateMapper)
                    .collect(Collectors.toList());

        } catch (ClassNotFoundException e) {
            throw new IOException("Ошибка при десериализации Binary-файла", e);
        }
    }
}
