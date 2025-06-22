package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkLogStatus {

    ACTIVE("Активный"),
    EDITED("Изменен"),
    DELETED("Удален"),
    ;

    private final String description;
}
