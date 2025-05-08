package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
@Tag(name = "Token API", description = "Управление токенами пользователей")
public class TokenController {

    private final JwtTokenService jwtTokenService;

    @GetMapping("/refresh")
    @Operation(summary = "Обновление пары access/refresh токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    public ResponseEntity<TokenResponse> refresh() {
        return ResponseEntity.ok(jwtTokenService.refreshTokens());
    }
}
