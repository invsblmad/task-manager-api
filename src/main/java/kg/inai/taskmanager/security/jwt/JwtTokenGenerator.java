package kg.inai.taskmanager.security.jwt;

import kg.inai.taskmanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtEncoder encoder;

    public String generateToken(UserDetails userDetails, int expirationTimeInHours) {
        JwtClaimsSet claims = extractJwtClaims(userDetails, expirationTimeInHours);
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private JwtClaimsSet extractJwtClaims(UserDetails userDetails, int expirationTimeInHours) {
        Instant now = Instant.now();
        User user = (User) userDetails;
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expirationTimeInHours, ChronoUnit.HOURS))
                .subject(userDetails.getUsername())
                .id(user.getId().toString())
                .claim("scope", getScope(userDetails))
                .build();
    }

    private String getScope(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
