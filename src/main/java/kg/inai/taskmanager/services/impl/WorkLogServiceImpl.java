package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.work_log.WorkLogRequestDto;
import kg.inai.taskmanager.dtos.work_log.WorkLogResponseDto;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.WorkLog;
import kg.inai.taskmanager.enums.WorkLogStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.mappers.WorkLogMapper;
import kg.inai.taskmanager.model.WorkLogRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.WorkLogService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import kg.inai.taskmanager.utils.TimeParserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;
    private final WorkLogMapper workLogMapper;
    private final MinioService minioService;
    private final AuthService authService;

    @Override
    public List<WorkLogResponseDto> getAllActive(String taskId) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return workLogRepository.findAllByTaskAndStatusNot(task, WorkLogStatus.DELETED).stream()
                .map(workLog -> workLogMapper.toDto(workLog, userMapper, minioService))
                .toList();
    }

    @Override
    public List<WorkLogResponseDto> getAll(String taskId) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return workLogRepository.findAllByTask(task).stream()
                .map(workLog -> workLogMapper.toDto(workLog, userMapper, minioService))
                .toList();
    }

    @Override
    public WorkLogResponseDto getById(Long id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запись в журнале работ не найдена"));

        return workLogMapper.toDto(workLog, userMapper, minioService);
    }

    @Override
    public WorkLogResponseDto save(String taskId, WorkLogRequestDto request) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        WorkLog workLog = workLogMapper.toEntity(request);
        workLog.setUser(authService.getAuthenticatedUser());
        workLog.setStatus(WorkLogStatus.ACTIVE);
        workLog.setTask(task);

        workLog = workLogRepository.save(workLog);
        return workLogMapper.toDto(workLog, userMapper, minioService);
    }

    @Override
    public void update(Long id, WorkLogRequestDto request) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запись в журнале работ не найдена"));

        workLog.setSpentMinutes(TimeParserUtil.parseToMinutes(request.spentTime()));
        workLog.setText(request.text());
        workLog.setStatus(WorkLogStatus.EDITED);
        workLogRepository.save(workLog);
    }

    @Override
    public void delete(Long id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запись в журнале работ не найдена"));

        workLog.setStatus(WorkLogStatus.DELETED);
        workLogRepository.save(workLog);
    }
}
