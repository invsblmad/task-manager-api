package kg.inai.taskmanager.enums;

public enum Role {
    ROLE_DEV("Разработчик"),
    ROLE_TEAM_LEAD("Тимлид"),

    ROLE_MANAGER("Менеджер"),
    ROLE_ADMIN("Администратор");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
