package kg.inai.taskmanager.models.tasks;

import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import kg.inai.taskmanager.models.user.UserResponse;

public record TaskResponse(Long id,
                           String code,
                           String name,
                           TaskPriority priority,
                           TaskType type,
                           TaskStatus status,
                           UserResponse user) {
}
