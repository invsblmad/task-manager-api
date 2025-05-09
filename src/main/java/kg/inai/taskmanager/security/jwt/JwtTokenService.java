package kg.inai.taskmanager.security.jwt;

import kg.inai.taskmanager.models.auth.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    TokenResponse generateTokens(UserDetails userDetails);
}
