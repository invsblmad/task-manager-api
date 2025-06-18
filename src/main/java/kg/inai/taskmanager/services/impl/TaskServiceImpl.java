package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.models.EnumModel;
import kg.inai.taskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Override
    public List<EnumModel> getAllowedStatusTransitions(TaskStatus status) {
        return TaskStatus.getAllowedTransitions(status).stream()
                .map(TaskStatus::toModel)
                .toList();
    }
}
