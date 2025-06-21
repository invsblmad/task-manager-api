package kg.inai.taskmanager.dtos.task;

import kg.inai.taskmanager.dtos.EnumDto;

import java.util.List;

public record TaskGroupResponse(EnumDto status,
                                int taskCount,
                                List<TaskResponse> tasks) {
}
