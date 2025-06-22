package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType {

    TASK("Задача"),
    SUBTASK("Подзадача"),
    BUG("Ошибка"),
    ;

    private final String description;
}
