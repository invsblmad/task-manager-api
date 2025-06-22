package kg.inai.taskmanager.security.jwt;

import kg.inai.taskmanager.dtos.auth.TokenResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    TokenResponseDto generateTokens(UserDetails userDetails);
}
