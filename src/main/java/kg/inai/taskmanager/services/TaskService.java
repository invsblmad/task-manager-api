package kg.inai.taskmanager.services;

import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.models.EnumModel;

import java.util.List;

public interface TaskService {

    List<EnumModel> getAllowedStatusTransitions(TaskStatus status);
}
