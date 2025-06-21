package kg.inai.taskmanager.dtos.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "Код не должен быть пустым")
        String code,

        @NotBlank(message = "Имя не должно быть пустым")
        String name) {
}
