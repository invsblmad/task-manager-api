package kg.inai.taskmanager.controllers;

import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.models.EnumModel;
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
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/statuses/{status}/transitions")
    public ResponseEntity<List<EnumModel>> getStatusTransitions(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getAllowedStatusTransitions(status));
    }
}
