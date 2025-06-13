package kg.inai.taskmanager.models.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kg.inai.taskmanager.enums.Role;

public record SignUpRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        String lastName,

        @NotBlank(message = "Почта не может быть пустой")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 5, message = "Пароль должен содержать минимум 5 символов")
        String password,

        @NotBlank(message = "Требуется подтверждение пароля")
        String passwordConfirmation,

        @NotNull(message = "Роль пользователя не может быть пустой")
        Role role
) {}