package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.*;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks API", description = "Управление задачами")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/statuses/{status}/transitions")
    @Operation(summary = "Получение статусов задачи, на которые можно перевести с текущего статуса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumDto.class)))),
    })
    public ResponseEntity<List<EnumDto>> getStatusTransitions(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getAllowedStatusTransitions(status));
    }

    @PutMapping("/{id}/to-status/{status}")
    @Operation(summary = "Изменение статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Переход на указанный статус невозможен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void moveToStatus(@PathVariable String id, @PathVariable TaskStatus status) {
        taskService.moveToStatus(id, status);
    }

    @GetMapping("/by-project/{projectCode}")
    @Operation(summary = "Получение всех задач проекта, отсортированных по статусам Backlog, В процессе, Готово")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskGroupResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<TaskGroupResponseDto> > getAll(@PathVariable String projectCode,
                                                              @RequestParam(required = false) boolean filterByCurrentUser) {
        return ResponseEntity.ok(taskService.getAll(projectCode, filterByCurrentUser));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TaskDetailedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TaskDetailedResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создание задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TaskDetailedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TaskDetailedResponseDto> save(@RequestPart("data") @Valid TaskCreateRequestDto request,
                                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok(taskService.save(request, files));
    }

    @PostMapping("/sub-task/for/{parentTaskId}")
    @Operation(summary = "Создание подзадачи для главной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TaskDetailedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TaskDetailedResponseDto> saveSubtask(@PathVariable String parentTaskId,
                                                               @RequestPart("data") @Valid TaskCreateRequestDto request,
                                                               @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok(taskService.saveSubtask(parentTaskId, request, files));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable String id,
                       @RequestPart("data") @Valid TaskUpdateRequestDto request,
                       @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        taskService.update(id, request, files);
    }

    @PostMapping("/{id}/generate")
    @Operation(summary = "Генерация подзадач с помощью AI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TaskDetailedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<GeneratedResultResponseDto> generateSubtasks(@PathVariable String id) {
        return ResponseEntity.ok(taskService.generateSubtasks(id));
    }

    @PostMapping("/sub-tasks/for/{parentTaskId}")
    @Operation(summary = "Сохранение сгенерированных подзадач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void saveSubtasks(@PathVariable String parentTaskId,
                             @Valid @RequestBody List<SubtaskCreateRequestDto> subtasks) {
        taskService.saveSubtasks(parentTaskId, subtasks);
    }
}
