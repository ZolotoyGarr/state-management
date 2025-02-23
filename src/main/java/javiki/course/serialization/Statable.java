package javiki.course.serialization;

public interface Statable<T> {
    void uploadState(T state);   // Загружает новое состояние
    T retrieveState();           // Возвращает текущее состояние (копию)
    Class<T> stateClass();       // Возвращает класс состояния
    StateType stateName();       // Возвращает имя состояния
}

