package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.TaskGroupResponse;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.mappers.TaskMapper;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.TaskService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final MinioService minioService;
    private final AuthService authService;

    @Override
    public List<TaskGroupResponse> getAll(String projectCode, boolean filterByCurrentUser) {
        List<TaskStatus> statuses = TaskStatus.getKanbanStatuses();
        return statuses.stream()
                .map(status -> groupTasks(projectCode, status, filterByCurrentUser))
                .toList();
    }


    @Override
    public List<EnumDto> getAllowedStatusTransitions(TaskStatus status) {
        return TaskStatus.getAllowedTransitions(status).stream()
                .map(TaskStatus::toModel)
                .toList();
    }

    @Override
    public void moveToStatus(String id, TaskStatus status) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(id))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (!TaskStatus.canTransitionTo(task.getStatus(), status)) {
            throw new TaskManagerException("Задача не может быть переведена в статус " + status);
        }

        task.setStatus(status);
        taskRepository.save(task);
    }

    private TaskGroupResponse groupTasks(String projectCode, TaskStatus status, boolean filterByCurrentUser) {
        List<Task> tasks = filterByCurrentUser
                ? taskRepository.findAllByIdProjectCodeAndStatusAndAssignedTo(projectCode, status, authService.getAuthenticatedUser())
                : taskRepository.findAllByIdProjectCodeAndStatus(projectCode, status);

        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> taskMapper.toDto(task, userMapper, minioService))
                .toList();

        return new TaskGroupResponse(
                new EnumDto(status.name(), status.getDescription()),
                taskResponses.size(),
                taskResponses
        );
    }
}
