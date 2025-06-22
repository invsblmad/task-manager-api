package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.TaskDetailedResponseDto;
import kg.inai.taskmanager.dtos.task.TaskGroupResponseDto;
import kg.inai.taskmanager.dtos.task.TaskRequestDto;
import kg.inai.taskmanager.dtos.task.TaskResponseDto;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.mappers.ProjectMapper;
import kg.inai.taskmanager.mappers.TaskMapper;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.model.AttachmentModel;
import kg.inai.taskmanager.repositories.ProjectRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.services.AttachmentService;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.TaskService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import kg.inai.taskmanager.utils.TimeEstimateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        return taskMapper.toDetailedDto(task, userMapper, projectMapper, minioService);
    }

    @Override
    public TaskDetailedResponseDto save(TaskRequestDto request, List<MultipartFile> files) {
        Project project = projectRepository.findByCode(request.projectCode())
                .orElseThrow(() -> new NotFoundException("Пррект не найден"));

        Task task = taskMapper.toEntity(request);
        task.setProject(project);
        task.setId(generateTaskId(project.getCode()));
        task.setStatus(TaskStatus.BACKLOG);

        User user = userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new NotFoundException("Не найден исполнитель задачи"));

        task.setAssignedTo(user);
        task.setCreatedBy(authService.getAuthenticatedUser());

        if (!StringUtils.isBlank(request.estimate())) {
            Long estimateMinutes = TimeEstimateUtil.parseToMinutes(request.estimate());
            Long remainingMinutes = StringUtils.isBlank(request.remainingTime())
                    ? estimateMinutes
                    : TimeEstimateUtil.parseToMinutes(request.remainingTime());

            task.setEstimateMinutes(estimateMinutes);
            task.setRemainingMinutes(remainingMinutes);
        }

        task = taskRepository.save(task);
        saveAttachments(task, files);
        return taskMapper.toDetailedDto(task, userMapper, projectMapper, minioService);
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

    private void saveAttachments(Task task, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String path = minioService.save(file, null, FileType.TASK_ATTACHMENT.getPath());

            attachmentService.save(AttachmentModel.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storagePath(path)
                    .contentType(file.getContentType())
                    .task(task)
                    .user(authService.getAuthenticatedUser())
                    .build());
        }
    }
}
