package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.models.EnumModel;
import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<EnumModel>> getStatusTransitions(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getAllowedStatusTransitions(status));
    }
}
