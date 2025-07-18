package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.team.TeamRequestDto;
import kg.inai.taskmanager.dtos.team.TeamResponseDto;
import kg.inai.taskmanager.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
@Tag(name = "Teams API", description = "Управление командами")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/active")
    @Operation(summary = "Получение списка активных команд")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeamResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<TeamResponseDto>> getAllActive() {
        return ResponseEntity.ok(teamService.getAllActive());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение списка всех команд")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeamResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    @Operation(summary = "Создание команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TeamResponseDto> save(@Valid @RequestBody TeamRequestDto teamRequest) {
        return ResponseEntity.ok(teamService.save(teamRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    @Operation(summary = "Изменение названия команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody TeamRequestDto teamRequest) {
        teamService.update(id, teamRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
