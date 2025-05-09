package kg.inai.taskmanager.models.user;

public record UserResponse(String email,
                           String firstName,
                           String lastName,
                           String avatarUrl,
                           String role) {
}
