package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatus {

    ACTIVE("Активный"),
    CLOSED("Закрыт"),
    DELETED("Удален"),
    ;

    private final String description;
}
