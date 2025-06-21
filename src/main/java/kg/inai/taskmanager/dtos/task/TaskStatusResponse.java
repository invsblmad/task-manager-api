package kg.inai.taskmanager.dtos.task;

import kg.inai.taskmanager.dtos.EnumDto;

public record TaskStatusResponse(EnumDto status,
                                 Integer taskCount) {
}
