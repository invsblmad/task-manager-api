package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.auth.SignInRequestDto;
import kg.inai.taskmanager.dtos.auth.SignUpRequestDto;
import kg.inai.taskmanager.dtos.auth.TokenResponseDto;
import kg.inai.taskmanager.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Вход и регистрация пользователей")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    @Operation(summary = "Аутентификация пользователя и получение пары access/refresh токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    public ResponseEntity<TokenResponseDto> signIn(@Valid @RequestBody SignInRequestDto signInRequest) {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация нового пользователя и получение пары access/refresh токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),
    })
    public ResponseEntity<TokenResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
}
