package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.*;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.mappers.ProjectMapper;
import kg.inai.taskmanager.mappers.TaskMapper;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.repositories.ProjectRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.services.AttachmentService;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.TaskService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import kg.inai.taskmanager.utils.TimeParserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final MinioService minioService;
    private final AuthService authService;
    private final AttachmentService attachmentService;

    @Override
    public List<TaskGroupResponseDto> getAll(String projectCode, boolean filterByCurrentUser) {
        List<TaskStatus> statuses = TaskStatus.getKanbanStatuses();
        return statuses.stream()
                .map(status -> groupTasks(projectCode, status, filterByCurrentUser))
                .toList();
    }

    @Override
    public TaskDetailedResponseDto getById(String id) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(id))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        TaskTimeProgressDto progress = buildProgress(task);

        List<SubtaskResponseDto> subtasks = task.getSubtasks().stream()
                .map(s -> taskMapper.toSubtaskDto(s, buildProgress(s), userMapper, minioService))
                .toList();

        return taskMapper.toDetailedDto(
                task, progress, subtasks, userMapper, projectMapper, minioService
        );
    }

    @Override
    public TaskDetailedResponseDto save(TaskRequestDto request, List<MultipartFile> files) {
        Task task = createTask(null, request, files);
        TaskTimeProgressDto progress = buildProgress(task);

        return taskMapper.toDetailedDto(
                task, progress, Collections.emptyList(), userMapper, projectMapper, minioService
        );
    }

    @Override
    public TaskDetailedResponseDto saveSubtask(String parentTaskId, TaskRequestDto request, List<MultipartFile> files) {
        Task task = createTask(parentTaskId, request, files);
        TaskTimeProgressDto progress = buildProgress(task);

        return taskMapper.toDetailedDto(
                task, progress, Collections.emptyList(), userMapper, projectMapper, minioService
        );
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

    private TaskGroupResponseDto groupTasks(String projectCode, TaskStatus status, boolean filterByCurrentUser) {
        List<Task> tasks = filterByCurrentUser
                ? taskRepository.findAllByIdProjectCodeAndStatusAndAssignedTo(projectCode, status, authService.getAuthenticatedUser())
                : taskRepository.findAllByIdProjectCodeAndStatus(projectCode, status);

        List<TaskResponseDto> taskResponses = tasks.stream()
                .map(task -> taskMapper.toDto(task, userMapper, minioService))
                .toList();

        return new TaskGroupResponseDto(
                new EnumDto(status.name(), status.getDescription()),
                taskResponses.size(),
                taskResponses
        );
    }

    private TaskId generateTaskId(String projectCode) {
        Long maxSeq = taskRepository.findMaxSequenceNumberByProjectCode(projectCode);
        Long newSeq = (maxSeq == null) ? 1L : maxSeq + 1;

        return new TaskId(projectCode, newSeq);
    }

    private Task createTask(String parentTaskId, TaskRequestDto request, List<MultipartFile> files) {
        Project project = projectRepository.findByCode(request.projectCode())
                .orElseThrow(() -> new NotFoundException("Пррект не найден"));

        User user = userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new NotFoundException("Не найден исполнитель задачи"));

        Task parentTask = null;
        if (!StringUtils.isBlank(parentTaskId)) {
            parentTask = taskRepository.findById(TaskIdParsesUtil.parse(parentTaskId))
                    .orElseThrow(() -> new NotFoundException("Главная задача не найдена"));
        }

        Task task = taskMapper.toEntity(request);
        task.setProject(project);
        task.setId(generateTaskId(project.getCode()));
        task.setStatus(TaskStatus.BACKLOG);
        task.setAssignedTo(user);
        task.setCreatedBy(authService.getAuthenticatedUser());
        task.setParentTask(parentTask);

        applyEstimates(task, request);

        task = taskRepository.save(task);
        attachmentService.save(task, files);
        return task;
    }

    private void applyEstimates(Task task, TaskRequestDto request) {
        if (StringUtils.isBlank(request.estimate())) {
            return;
        }

        Long estimate = TimeParserUtil.parseToMinutes(request.estimate());
        Long remaining = StringUtils.isBlank(request.remainingTime())
                ? estimate
                : TimeParserUtil.parseToMinutes(request.remainingTime());

        task.setEstimateMinutes(estimate);
        task.setRemainingMinutes(remaining);
    }

    private TaskTimeProgressDto buildProgress(Task task) {
        long estimate = task.getEstimateMinutes();
        long remaining = task.getRemainingMinutes();
        long spent = estimate - remaining;

        int spentPercent = (int) ((double) spent / estimate * 100);
        int remainingPercent = 100 - spentPercent;

        return TaskTimeProgressDto.builder()
                .estimatedTime(TimeParserUtil.formatFromMinutes(estimate))
                .spentTime(TimeParserUtil.formatFromMinutes(spent))
                .remainingTime(TimeParserUtil.formatFromMinutes(remaining))
                .spentPercent(spentPercent)
                .remainingPercent(remainingPercent)
                .build();
    }
}
