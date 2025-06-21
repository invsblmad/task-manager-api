package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Page<TaskResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<EnumDto> getAllowedStatusTransitions(TaskStatus status) {
        return TaskStatus.getAllowedTransitions(status).stream()
                .map(TaskStatus::toModel)
                .toList();
    }

    @Override
    public void moveToStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (!TaskStatus.canTransitionTo(task.getStatus(), status)) {
            throw new TaskManagerException("Задача не может быть переведена в статус " + status);
        }

        task.setStatus(status);
        taskRepository.save(task);
    }
}
