package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.*;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assignedTo", expression = "java(userMapper.toShortDto(task.getAssignedTo(), minioService))")
    TaskResponseDto toDto(Task task,
                          @Context UserMapper userMapper,
                          @Context MinioService minioService);

    @Mapping(target = "assignedTo", expression = "java(userMapper.toShortDto(task.getAssignedTo(), minioService))")
    @Mapping(target = "createdBy", expression = "java(userMapper.toShortDto(task.getCreatedBy(), minioService))")
    @Mapping(target = "project", expression = "java(projectMapper.toDto(task.getProject(), minioService))")
    @Mapping(target = "progress", source = "progress")
    @Mapping(target = "subtasks", source = "subtasks")
    TaskDetailedResponseDto toDetailedDto(Task task,
                                          TaskTimeProgressDto progress,
                                          List<SubtaskResponseDto> subtasks,
                                          @Context UserMapper userMapper,
                                          @Context ProjectMapper projectMapper,
                                          @Context MinioService minioService);

    @Mapping(target = "progress", source = "progress")
    @Mapping(target = "assignedTo", expression = "java(userMapper.toShortDto(task.getAssignedTo(), minioService))")
    SubtaskResponseDto toSubtaskDto(Task task,
                                    TaskTimeProgressDto progress,
                                    @Context UserMapper userMapper,
                                    @Context MinioService minioService);

    Task toEntity(TaskCreateRequestDto taskRequest);

    default String map(TaskId taskId) {
        return TaskIdParsesUtil.format(taskId);
    }

    default EnumDto enumToDto(TaskPriority priority) {
        if (priority == null) return null;
        return new EnumDto(priority.name(), priority.getDescription());
    }

    default EnumDto enumToDto(TaskType type) {
        if (type == null) return null;
        return new EnumDto(type.name(), type.getDescription());
    }

    default EnumDto enumToDto(TaskStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }

    default EnumDto enumToDto(ProjectStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
