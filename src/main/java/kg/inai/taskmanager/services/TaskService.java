package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Page<TaskResponse> getAll(Pageable pageable);

    List<EnumDto> getAllowedStatusTransitions(TaskStatus status);

    void moveToStatus(Long id, TaskStatus status);
}
