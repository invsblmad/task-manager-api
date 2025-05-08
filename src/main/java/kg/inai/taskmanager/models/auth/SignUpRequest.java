package kg.inai.taskmanager.models.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kg.inai.taskmanager.enums.Role;

public record SignUpRequest(
        @NotBlank(message = "The first name can't be null or empty")
        String firstName,

        @NotBlank(message = "The last name can't be null or empty")
        String lastName,

        @NotBlank(message = "The email can't be null or empty")
        String email,

        @NotBlank(message = "The password can't be null or empty")
        @Size(min = 5, message = "The password must be at least 5 characters long")
        String password,

        String passwordConfirmation,
        @NotNull(message = "User role can't be null")
        Role role
) {}