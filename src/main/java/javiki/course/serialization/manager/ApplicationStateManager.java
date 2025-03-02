package javiki.course.serialization.manager;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;
import javiki.course.serialization.serializer.StateSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationStateManager implements StateManager {

    private final String stateFolderPath;
    private final StateSerializer serializer;
    private final ApplicationStateHolder stateHolder;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH-mm-ss")
            .withZone(ZoneId.of("UTC"));

    public ApplicationStateManager(String stateFolderPath, StateSerializer serializer) {
        this.stateFolderPath = stateFolderPath;
        this.serializer = serializer;
        this.stateHolder = ApplicationStateHolder.getInstance();
    }

    @Override
    public void register(Statable<?> statable) {
        stateHolder.register(statable);
    }

    private void save(Map<StateType, List<Statable<?>>> stateToSave) {
        for (Map.Entry<StateType, List<Statable<?>>> entry : stateToSave.entrySet()) {
            StateType stateName = entry.getKey();
            List<Statable<?>> statables = entry.getValue();

            if (statables.isEmpty()) {
                System.out.println("⚠️ Пропущено сохранение пустого состояния: " + stateName.getReadableName());
                continue;
            }

            String dateFolder = DATE_FORMATTER.format(Instant.now());
            String timeStamp = TIME_FORMATTER.format(Instant.now());

            Path dirPath = Paths.get(stateFolderPath, stateName.getReadableName(), dateFolder);
            Path filePath = dirPath.resolve(timeStamp + serializer.getFileExtension());

            try {
                Files.createDirectories(dirPath);
                serializer.serializeAndSave(filePath, statables);
                System.out.println("✅ Сохранено " + statables.size() + " объектов в " + filePath);
            } catch (IOException e) {
                System.err.println("❌ Ошибка сохранения в " + filePath + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Map<StateType, List<Statable<?>>> download() {
        Map<StateType, List<Statable<?>>> loadedStates = new HashMap<>();

        try (Stream<Path> paths = Files.list(Paths.get(stateFolderPath))) {
            List<Path> stateDirectories = paths.filter(Files::isDirectory).collect(Collectors.toList());

            for (Path stateDir : stateDirectories) {
                String folderName = stateDir.getFileName().toString();
                StateType stateType;

                try {
                    stateType = StateType.fromFolderName(folderName);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Пропущена неизвестная директория: " + folderName);
                    continue;
                }

                Path latestFile = getLatestStateFile(stateDir);
                if (latestFile == null || !Files.exists(latestFile)) {
                    System.out.println("❌ Ошибка: Нет доступных файлов для загрузки из " + stateDir);
                    continue;
                }

                System.out.println("📥 Загрузка состояния из файла: " + latestFile);

                try {
                    List<Statable<?>> stateObjects = serializer.downloadAndDeserialize(latestFile, stateType);
                    if (stateObjects.isEmpty()) {
                        System.out.println("⚠️ Внимание: Файл " + latestFile + " пуст или содержит некорректные данные.");
                        continue;
                    }
                    loadedStates.put(stateType, stateObjects);
                    System.out.println("✅ Загружено " + stateObjects.size() + " объектов из " + latestFile);
                } catch (IOException e) {
                    System.err.println("❌ Ошибка при загрузке состояния из " + latestFile + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка при получении списка папок состояний: " + e.getMessage());
            e.printStackTrace();
        }

        if (loadedStates.isEmpty()) {
            System.out.println("⚠️ Внимание: Не удалось загрузить ни одного состояния!");
        }

        return loadedStates;
    }

    private Path getLatestStateFile(Path stateDir) throws IOException {
        try (Stream<Path> dateDirs = Files.list(stateDir)) {
            Optional<Path> latestDateDir = dateDirs
                    .filter(Files::isDirectory)
                    .max(Comparator.comparing(Path::getFileName));

            if (latestDateDir.isPresent()) {
                try (Stream<Path> files = Files.list(latestDateDir.get())) {
                    return files
                            .filter(Files::isRegularFile) // Фильтруем только файлы
                            .filter(file -> file.toString().endsWith(serializer.getFileExtension())) // Проверяем расширение
                            .max(Comparator.comparing(Path::getFileName))
                            .orElse(null);
                }
            }
        }
        return null;
    }

    @Override
    public void loadGlobalState() {
        if (!stateHolder.getStateList().isEmpty()) {
            System.out.println("⚠️ Состояние уже загружено, пропускаем повторную загрузку.");
            return;
        }

        stateHolder.clear();
        Map<StateType, List<Statable<?>>> loadedStates = download();
        List<Statable<?>> allStates = new ArrayList<>();

        for (List<Statable<?>> statables : loadedStates.values()) {
            allStates.addAll(statables);
        }

        stateHolder.setStateList(allStates);
        System.out.println("✅ Загружено " + allStates.size() + " объектов в ApplicationStateHolder");
    }

    @Override
    public void saveGlobalState() {
        Map<StateType, List<Statable<?>>> stateToSave = new HashMap<>();

        for (Statable<?> statable : stateHolder.getStateList()) {
            StateType stateName = statable.stateName();
            stateToSave.computeIfAbsent(stateName, k -> new ArrayList<>()).add(statable);
        }

        save(stateToSave);
        System.out.println("✅ Сохранено " + stateHolder.getStateList().size() + " объектов.");
    }
}
