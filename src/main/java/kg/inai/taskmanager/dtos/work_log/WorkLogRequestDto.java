package kg.inai.taskmanager.dtos.work_log;

import jakarta.validation.constraints.NotBlank;

public record WorkLogRequestDto(
        @NotBlank(message = "Затраченное время не заполнено")
        String spentTime,

        @NotBlank(message = "Текст не заполнен")
        String text) {
}
