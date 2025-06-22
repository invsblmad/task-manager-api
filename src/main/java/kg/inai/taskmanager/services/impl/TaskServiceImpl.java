package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.*;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.mappers.ProjectMapper;
import kg.inai.taskmanager.mappers.TaskMapper;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.repositories.ProjectRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.services.*;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import kg.inai.taskmanager.utils.TimeParserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final OpenAiService openAiService;

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
    public TaskDetailedResponseDto save(TaskCreateRequestDto request, List<MultipartFile> files) {
        Task task = createTask(null, request, files);
        TaskTimeProgressDto progress = buildProgress(task);

        return taskMapper.toDetailedDto(
                task, progress, Collections.emptyList(), userMapper, projectMapper, minioService
        );
    }

    @Override
    public TaskDetailedResponseDto saveSubtask(String parentTaskId, TaskCreateRequestDto request, List<MultipartFile> files) {
        Task task = createTask(parentTaskId, request, files);
        TaskTimeProgressDto progress = buildProgress(task);

        return taskMapper.toDetailedDto(
                task, progress, Collections.emptyList(), userMapper, projectMapper, minioService
        );
    }

    @Override
    public void update(String id, TaskUpdateRequestDto request, List<MultipartFile> files) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(id))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        User user = userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new NotFoundException("Не найден исполнитель задачи"));

        task.setAssignedTo(user);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());

        applyEstimates(task, request.estimate(), request.remainingTime());
        taskRepository.save(task);
        attachmentService.save(task, files);
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

    @Override
    public GeneratedResultResponseDto generateSubtasks(String id) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(id))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        List<GeneratedSubtaskDto> subtasks = openAiService.generateSubtasks(task.getDescription());
        return new GeneratedResultResponseDto(task.getTitle(), subtasks);
    }

    @Override
    public void saveSubtasks(String parentTaskId, List<SubtaskCreateRequestDto> subtasks) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(parentTaskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        Long maxSeq = taskRepository.findMaxSequenceNumberByProjectCode(task.getProject().getCode());
        long currentSeq = (maxSeq == null) ? 1L : maxSeq + 1;

        List<Task> result = new ArrayList<>();
        for (SubtaskCreateRequestDto request : subtasks) {
            TaskId newTaskId = new TaskId(task.getProject().getCode(), currentSeq++);
            result.add(buildSubtask(task, request, newTaskId));
        }

        taskRepository.saveAll(result);
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

    private Task createTask(String parentTaskId, TaskCreateRequestDto request, List<MultipartFile> files) {
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

        applyEstimates(task, request.estimate(), request.remainingTime());

        task = taskRepository.save(task);
        attachmentService.save(task, files);
        return task;
    }

    private void applyEstimates(Task task, String estimate, String remainingTime) {
        if (StringUtils.isBlank(estimate)) {
            return;
        }

        Long estimateMinutes = TimeParserUtil.parseToMinutes(estimate);
        Long remainingMinutes = StringUtils.isBlank(remainingTime)
                ? estimateMinutes
                : TimeParserUtil.parseToMinutes(remainingTime);

        task.setEstimateMinutes(estimateMinutes);
        task.setRemainingMinutes(remainingMinutes);
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

    private Task buildSubtask(Task parentTask, SubtaskCreateRequestDto request, TaskId id) {
        return Task.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .type(TaskType.SUBTASK)
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.BACKLOG)
                .parentTask(parentTask)
                .project(parentTask.getProject())
                .createdBy(authService.getAuthenticatedUser())
                .assignedTo(authService.getAuthenticatedUser())
                .estimateMinutes(0L)
                .remainingMinutes(0L)
                .build();
    }
}
