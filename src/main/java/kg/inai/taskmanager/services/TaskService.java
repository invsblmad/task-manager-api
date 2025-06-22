package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.*;
import kg.inai.taskmanager.enums.TaskStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {

    List<TaskGroupResponseDto> getAll(String projectCode, boolean filterByCurrentUser);

    TaskDetailedResponseDto getById(String id);

    TaskDetailedResponseDto save(TaskCreateRequestDto request, List<MultipartFile> files);

    TaskDetailedResponseDto saveSubtask(String parentTaskId, TaskCreateRequestDto request, List<MultipartFile> files);

    void update(String id, TaskUpdateRequestDto request, List<MultipartFile> files);

    List<EnumDto> getAllowedStatusTransitions(TaskStatus status);

    void moveToStatus(String id, TaskStatus status);

    GeneratedResultResponseDto generateSubtasks(String id);
}
