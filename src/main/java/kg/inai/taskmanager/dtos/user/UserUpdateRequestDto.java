package kg.inai.taskmanager.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDto(
        @NotBlank(message = "Имя не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        String lastName,

        String phoneNumber,

        @NotBlank(message = "Должность не может быть пустой")
        String jobTitle,

        @NotBlank(message = "Отдел не может быть пустым")
        String department) {
}
