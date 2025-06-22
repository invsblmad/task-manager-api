package kg.inai.taskmanager.dtos.auth;

public record TokenResponseDto(String accessToken,
                               String refreshToken) {
}
