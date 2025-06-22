package kg.inai.taskmanager.dtos.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectUpdateRequest(

        @NotBlank(message = "Имя не должно быть пустым")
        String name) {
}