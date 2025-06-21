package kg.inai.taskmanager.dtos.team;

import jakarta.validation.constraints.NotBlank;

public record TeamRequest(
        @NotBlank(message = "Название команды не должно быть пустым")
        String name) {
}
