package kg.inai.taskmanager.models.auth;

public record TokenResponse(String accessToken,
                            String refreshToken) {
}
