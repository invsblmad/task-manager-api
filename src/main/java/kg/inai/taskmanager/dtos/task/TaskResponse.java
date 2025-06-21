package kg.inai.taskmanager.dtos.task;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.user.UserShortResponse;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import kg.inai.taskmanager.dtos.user.UserResponse;

public record TaskResponse(String id,
                           String title,
                           EnumDto priority,
                           EnumDto type,
                           EnumDto status,
                           UserShortResponse assignedTo) {
}
