package kg.inai.taskmanager.security.impl;

import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.security.jwt.JwtTokenService;
import kg.inai.taskmanager.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserDetailsService userDetailsService;

    @Value("${jwt-tokens.access.expiration-hours}")
    private int accessTokenExpirationHours;

    @Value("${jwt-tokens.refresh.expiration-hours}")
    private int refreshTokenExpirationHours;

    @Override
    public TokenResponse generateTokens(UserDetails userDetails) {
        String accessToken = jwtTokenGenerator.generateToken(userDetails, accessTokenExpirationHours);
        String refreshToken = jwtTokenGenerator.generateToken(userDetails, refreshTokenExpirationHours);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshTokens() {
        return generateTokens(getAuthenticatedUserDetails());
    }

    @Override
    public UserDetails getAuthenticatedUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt token) {
            String username = token.getSubject();
            return userDetailsService.loadUserByUsername(username);
        }
        return null;
    }
}
