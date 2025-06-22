package kg.inai.taskmanager.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequestDto(
        @NotBlank(message = "Почта не может быть пустой")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}
