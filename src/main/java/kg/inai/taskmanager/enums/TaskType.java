package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType {

    TASK("Задача"),
    BUG("Ошибка"),
    ;

    private final String description;
}
