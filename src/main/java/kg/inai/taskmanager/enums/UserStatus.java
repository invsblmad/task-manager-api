package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("Активный"),
    BLOCKED("Заблокированный"),
    ;

    private final String value;
}
