package kg.inai.taskmanager.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.inai.taskmanager.enums.TaskPriority;

public record TaskUpdateRequestDto(

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
