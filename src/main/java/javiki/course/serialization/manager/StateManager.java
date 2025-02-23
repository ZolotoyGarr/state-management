package javiki.course.serialization.manager;

import javiki.course.serialization.Statable;
import javiki.course.serialization.StateType;

import java.util.List;
import java.util.Map;

public interface StateManager {
    void saveGlobalState();
    void loadGlobalState();
    void registerBroke(Statable<?> statable);
    Map<StateType, List<Statable<?>>> download();
}
