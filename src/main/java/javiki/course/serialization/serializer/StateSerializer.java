package javiki.course.serialization.serializer;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StateSerializer {
    void serializeAndSave(Path filePath, List<Statable<?>> statables) throws IOException;
    List<Statable<?>> downloadAndDeserialize(Path filePath, StateType stateName) throws IOException;
    String getFileExtension();
}
