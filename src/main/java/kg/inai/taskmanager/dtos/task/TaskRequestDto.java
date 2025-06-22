package kg.inai.taskmanager.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskType;

public record TaskRequestDto(
        @NotBlank(message = "Проект не указан")
        String projectCode,

        @NotNull(message = "Тип задачи не указан")
        TaskType type,

        @NotBlank(message = "Тема не заполнена")
        String title,

        String description,

        @NotNull(message = "Приоритет не указан")
        TaskPriority priority,

        @NotNull(message = "Исполнитель не указан")
        Long assignedUserId,

        String estimate,
        String remainingTime
) {
}
