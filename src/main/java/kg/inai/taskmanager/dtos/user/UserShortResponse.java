package kg.inai.taskmanager.dtos.user;

public record UserShortResponse(Long id,
                               String firstName,
                               String lastName,
                               String avatarUrl) {
}
