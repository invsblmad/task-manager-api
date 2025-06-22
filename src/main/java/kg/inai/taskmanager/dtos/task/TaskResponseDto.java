package kg.inai.taskmanager.dtos.task;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.user.UserShortResponseDto;

public record TaskResponseDto(String id,
                              String title,
                              EnumDto priority,
                              EnumDto type,
                              EnumDto status,
                              UserShortResponseDto assignedTo) {
}
