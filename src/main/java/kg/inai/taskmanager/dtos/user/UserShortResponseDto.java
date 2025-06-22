package kg.inai.taskmanager.dtos.user;

public record UserShortResponseDto(Long id,
                                   String firstName,
                                   String lastName,
                                   String avatarUrl) {
}
