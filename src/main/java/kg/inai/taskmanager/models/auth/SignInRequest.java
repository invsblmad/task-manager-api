package kg.inai.taskmanager.models.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "Почта не может быть пустой")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}
