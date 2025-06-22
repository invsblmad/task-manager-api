package kg.inai.taskmanager.dtos.task;

import kg.inai.taskmanager.dtos.EnumDto;

public record TaskStatusResponseDto(EnumDto status,
                                    Integer taskCount) {
}
