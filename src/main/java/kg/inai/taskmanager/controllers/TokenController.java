package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.inai.taskmanager.dtos.auth.TokenResponseDto;
import kg.inai.taskmanager.security.jwt.JwtTokenService;
import kg.inai.taskmanager.services.AuthService;
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

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/refresh")
    @Operation(summary = "Обновление пары access/refresh токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TokenResponseDto> refresh() {
        return ResponseEntity.ok(jwtTokenService.generateTokens(authService.getAuthenticatedUser()));
    }
}
