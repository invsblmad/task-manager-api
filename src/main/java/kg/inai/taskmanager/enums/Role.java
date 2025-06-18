package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    DEV("Разработчик"),
    TEAM_LEAD("Тимлид"),
    ADMIN("Администратор"),
    ;

    private final String value;
}
