package kg.inai.taskmanager.security.impl;

import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.security.jwt.JwtTokenGenerator;
import kg.inai.taskmanager.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenGenerator jwtTokenGenerator;

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
}
