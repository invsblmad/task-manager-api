package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskPriority {

    HIGHEST("Наивысший"),
    HIGH("Высокий"),
    MEDIUM("Средний"),
    LOW("Низкий"),
    ;

    private final String description;
}
