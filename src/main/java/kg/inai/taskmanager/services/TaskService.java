package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.TaskGroupResponse;
import kg.inai.taskmanager.dtos.task.TaskStatusResponse;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskGroupResponse> getAll(String projectCode, boolean filterByCurrentUser);

    List<EnumDto> getAllowedStatusTransitions(TaskStatus status);

    void moveToStatus(String id, TaskStatus status);
}
