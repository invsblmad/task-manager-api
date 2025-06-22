package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.TaskRequestDto;
import kg.inai.taskmanager.dtos.task.TaskDetailedResponseDto;
import kg.inai.taskmanager.dtos.task.TaskGroupResponseDto;
import kg.inai.taskmanager.enums.TaskStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {

    List<TaskGroupResponseDto> getAll(String projectCode, boolean filterByCurrentUser);

    TaskDetailedResponseDto getById(String id);

    TaskDetailedResponseDto save(TaskRequestDto request, List<MultipartFile> files);

    TaskDetailedResponseDto saveSubtask(String parentTaskId, TaskRequestDto request, List<MultipartFile> files);

    List<EnumDto> getAllowedStatusTransitions(TaskStatus status);

    void moveToStatus(String id, TaskStatus status);
}
