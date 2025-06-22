package kg.inai.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.inai.taskmanager.dtos.project.ProjectRequestDto;
import kg.inai.taskmanager.dtos.project.ProjectResponseDto;
import kg.inai.taskmanager.dtos.project.ProjectUpdateRequestDto;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects API", description = "Управление проектами")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/active")
    @Operation(summary = "Получение списка активных проектов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<ProjectResponseDto>> getAllActive() {
        return ResponseEntity.ok(projectService.getAllActive());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех проектов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Page<ProjectResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(projectService.getAll(pageable));
    }

    @GetMapping("/{code}")
    @Operation(summary = "Получение проекта по коду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ProjectResponseDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(projectService.getByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = ProjectResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ProjectResponseDto> save(@RequestPart(name = "data") @Valid ProjectRequestDto request,
                                                   @RequestPart(name = "file", required = false) MultipartFile image) {
        return ResponseEntity.ok(projectService.save(request, image));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление данных проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void update(@PathVariable String code,
                       @RequestPart(name = "data", required = false) @Valid ProjectUpdateRequestDto request,
                       @RequestPart(name = "file", required = false) MultipartFile image) {
        projectService.update(code, request, image);
    }

    @PutMapping("/{code}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменение статуса проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void updateStatus(@PathVariable String code, @PathVariable ProjectStatus status) {
        projectService.updateStatus(code, status);
    }
}
