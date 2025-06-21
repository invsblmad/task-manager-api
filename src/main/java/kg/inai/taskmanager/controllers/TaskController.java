package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import kg.inai.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void moveToStatus(@PathVariable Long id, @PathVariable TaskStatus status) {
        taskService.moveToStatus(id, status);
    }

    @GetMapping
    @Operation(summary = "Получение всех задач с пагинацией")
    public ResponseEntity<Page<TaskResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAll(pageable));
    }

}
