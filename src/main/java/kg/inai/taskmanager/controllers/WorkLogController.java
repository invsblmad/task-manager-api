package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.work_log.WorkLogRequestDto;
import kg.inai.taskmanager.dtos.work_log.WorkLogResponseDto;
import kg.inai.taskmanager.services.WorkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/work-logs")
@RequiredArgsConstructor
@Tag(name = "Work logs API", description = "Управление журналом работ")
public class WorkLogController {

    private final WorkLogService workLogService;

    @GetMapping("/by-task/{taskId}/active")
    @Operation(summary = "Получение активных журналов работ задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkLogResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<WorkLogResponseDto>> getAllActive(@PathVariable String taskId) {
        return ResponseEntity.ok(workLogService.getAllActive(taskId));
    }

    @GetMapping("/by-task/{taskId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех журналов работ задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkLogResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<WorkLogResponseDto>> getAll(@PathVariable String taskId) {
        return ResponseEntity.ok(workLogService.getAll(taskId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение журнала работ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = WorkLogResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<WorkLogResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workLogService.getById(id));
    }

    @PostMapping("/{taskId}")
    @Operation(summary = "Создание записи в журнал работ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = WorkLogResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<WorkLogResponseDto> save(@PathVariable String taskId,
                                                   @Valid @RequestBody WorkLogRequestDto request) {
        return ResponseEntity.ok(workLogService.save(taskId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение записи в журнале работ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody WorkLogRequestDto request) {
        workLogService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление записи в журнале работ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void delete(@PathVariable Long id) {
        workLogService.delete(id);
    }
}
