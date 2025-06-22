package kg.inai.taskmanager.dtos.task;

import jakarta.validation.constraints.NotBlank;

public record SubtaskCreateRequestDto(
        @NotBlank(message = "Тема не заполнена")
        String title,

        @NotBlank(message = "Описание не заполнено")
        String description) {
}
