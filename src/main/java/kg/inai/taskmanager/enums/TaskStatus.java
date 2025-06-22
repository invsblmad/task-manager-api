package kg.inai.taskmanager.enums;

import kg.inai.taskmanager.dtos.EnumDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum TaskStatus {

    BACKLOG("Backlog"),
    IN_ANALYSIS("В анализе"),
    IN_PROGRESS("В работе"),
    PAUSED("На паузе"),
    WAITING_FOR_TESTING("Ожидание тестирования"),
    TESTING("На тестировании"),
    ON_REVIEW("На ревью"),
    TO_MERGE("Проверено"),
    DONE("Готово"),
    CANCELLED("Отменен"),
    ;

    private final String description;

    private static final Map<TaskStatus, Set<TaskStatus>> TRANSITIONS = Map.of(
            TaskStatus.BACKLOG, Set.of(TaskStatus.IN_ANALYSIS, TaskStatus.IN_PROGRESS),
            TaskStatus.IN_ANALYSIS, Set.of(TaskStatus.IN_PROGRESS, TaskStatus.PAUSED),
            TaskStatus.IN_PROGRESS, Set.of(TaskStatus.IN_ANALYSIS, TaskStatus.WAITING_FOR_TESTING, TaskStatus.PAUSED, TaskStatus.CANCELLED),
            TaskStatus.PAUSED, Set.of(TaskStatus.IN_ANALYSIS, TaskStatus.IN_PROGRESS, TaskStatus.CANCELLED),
            TaskStatus.WAITING_FOR_TESTING, Set.of(TaskStatus.TESTING, TaskStatus.IN_PROGRESS),
            TaskStatus.TESTING, Set.of(TaskStatus.ON_REVIEW, TaskStatus.TO_MERGE, TaskStatus.IN_PROGRESS),
            TaskStatus.ON_REVIEW, Set.of(TaskStatus.TO_MERGE, TaskStatus.TESTING),
            TaskStatus.TO_MERGE, Set.of(TaskStatus.DONE, TaskStatus.TESTING),
            TaskStatus.DONE, Set.of(),
            TaskStatus.CANCELLED, Set.of()
    );

    public static boolean canTransitionTo(TaskStatus source, TaskStatus target) {
        return TRANSITIONS.getOrDefault(source, Set.of()).contains(target);
    }

    public static Set<TaskStatus> getAllowedTransitions(TaskStatus status) {
        return TRANSITIONS.getOrDefault(status, Set.of());
    }

    public static List<TaskStatus> getKanbanStatuses() {
        return List.of(BACKLOG, IN_PROGRESS, DONE);
    }

    public static EnumDto toModel(TaskStatus status) {
        return new EnumDto(status.name(), status.getDescription());
    }
}
