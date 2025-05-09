package kg.inai.taskmanager.enums;

public enum Role {
    DEV("Разработчик"),
    TEAM_LEAD("Тимлид"),

    MANAGER("Менеджер"),
    ADMIN("Администратор");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
