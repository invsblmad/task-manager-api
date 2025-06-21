package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.task.TaskResponse;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import kg.inai.taskmanager.enums.UserStatus;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", expression = "java(kg.inai.taskmanager.utils.TaskIdParsesUtil.format(task.getId()))")
    @Mapping(target = "assignedTo", expression = "java(userMapper.toShortDto(task.getAssignedTo(), minioService))")
    TaskResponse toDto(Task task, @Context MinioService minioService, @Context UserMapper userMapper);

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

    default EnumDto enumToDto(UserStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
