package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.comment.CommentRequestDto;
import kg.inai.taskmanager.dtos.comment.CommentResponseDto;
import kg.inai.taskmanager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comments API", description = "Управление комментариями")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/by-task/{taskId}/active")
    @Operation(summary = "Получение активных комментариев задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<CommentResponseDto>> getAllActive(@PathVariable String taskId) {
        return ResponseEntity.ok(commentService.getAllActive(taskId));
    }

    @GetMapping("/by-task/{taskId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех комментариев задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<CommentResponseDto>> getAll(@PathVariable String taskId) {
        return ResponseEntity.ok(commentService.getAll(taskId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<CommentResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PostMapping("/{taskId}")
    @Operation(summary = "Создание комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<CommentResponseDto> save(@PathVariable String taskId,
                                                   @Valid @RequestBody CommentRequestDto commentRequest) {
        return ResponseEntity.ok(commentService.save(taskId, commentRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody CommentRequestDto commentRequest) {
        commentService.update(id, commentRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
