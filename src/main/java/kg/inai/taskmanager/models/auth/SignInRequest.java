package kg.inai.taskmanager.models.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "The email can't be null or empty")
        String email,

        @NotBlank(message = "The password can't be null or empty")
        String password
) {}
