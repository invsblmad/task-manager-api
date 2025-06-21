package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.models.user.UserDetailedResponse;
import kg.inai.taskmanager.models.user.UserResponse;
import kg.inai.taskmanager.models.user.UserUpdateRequest;
import kg.inai.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users API", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение списка всех пользователей с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Page<UserResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Получение списка активных пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<UserResponse>> getAllActive() {
        return ResponseEntity.ok(userService.getAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = UserDetailedResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDetailedResponse> getAll(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Блокировака пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void block(@PathVariable Long id) {
        userService.block(id);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение данных авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = UserDetailedResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDetailedResponse> getAuthenticatedUser() {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение данных пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable Long id,
                       @RequestPart(name = "data") @Valid UserUpdateRequest request,
                       @RequestPart(name = "file", required = false) MultipartFile avatar) {
        userService.update(id, request, avatar);
    }

    @GetMapping("/by-team/{teamId}")
    @Operation(summary = "Получение пользователей по команде")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
        })
    public List<UserResponse> getByTeam(@PathVariable Long teamId) {
        return userService.getByTeam(teamId);
    }

    @PutMapping("/{userId}/add-to-team/{teamId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    @Operation(summary = "Добавление пользователя в команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void addToTeam(@PathVariable Long userId, @PathVariable Long teamId) {
        userService.addToTeam(userId, teamId);
    }

    @PutMapping("/{userId}/delete-from-team")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    @Operation(summary = "Удаление пользователя из команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void addToTeam(@PathVariable Long userId) {
        userService.deleteFromTeam(userId);
    }
}
