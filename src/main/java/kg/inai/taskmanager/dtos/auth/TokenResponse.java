package kg.inai.taskmanager.dtos.auth;

public record TokenResponse(String accessToken,
                            String refreshToken) {
}
