package javiki.course.serialization.manager;

import javiki.course.serialization.Statable;

import java.util.ArrayList;
import java.util.List;

public class ApplicationStateHolder {
    private static final ApplicationStateHolder INSTANCE = new ApplicationStateHolder();

    private final List<Statable<?>> stateList = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ApplicationStateHolder() {}

    // Public method to provide access to the single instance
    public static ApplicationStateHolder getInstance() {
        return INSTANCE;
    }

    public void register(Statable<?> statableObj) {
        stateList.add(statableObj);
    }

    public void setStateList(List<Statable<?>> newStateList) {
        stateList.clear();
        stateList.addAll(new ArrayList<>(newStateList)); // Создаём копию входного списка
    }

    public List<Statable<?>> getStateList() {
        return new ArrayList<>(stateList); // Возвращаем неизменяемый список
    }

    public void clear() {
        stateList.clear();
    }

}
