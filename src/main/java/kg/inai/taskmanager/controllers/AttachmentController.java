package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.inai.taskmanager.dtos.AttachmentResponseDto;
import kg.inai.taskmanager.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachments API", description = "Управление вложениями задачи")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/{taskId}")
    @Operation(summary = "Получение списка вложений задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttachmentResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<AttachmentResponseDto>> getAll(@PathVariable String taskId) {
        return ResponseEntity.ok(attachmentService.getAll(taskId));
    }

    @PostMapping("/{taskId}")
    @Operation(summary = "Сохрание списка вложений в задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void save(@PathVariable String taskId, @RequestParam List<MultipartFile> files) {
        attachmentService.save(taskId, files);
    }
}
