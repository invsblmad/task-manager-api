package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.work_log.WorkLogRequestDto;
import kg.inai.taskmanager.dtos.work_log.WorkLogResponseDto;
import kg.inai.taskmanager.entities.WorkLog;
import kg.inai.taskmanager.enums.WorkLogStatus;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.utils.TimeParserUtil;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkLogMapper {

    @Mapping(target = "spentTime", source = "spentMinutes")
    @Mapping(target = "user", expression = "java(userMapper.toShortDto(workLog.getUser(), minioService))")
    WorkLogResponseDto toDto(WorkLog workLog,
                             @Context UserMapper userMapper,
                             @Context MinioService minioService);

    @Mapping(target = "spentMinutes", source = "spentTime")
    WorkLog toEntity(WorkLogRequestDto workLogRequestDto);

    default String map(Long spentMinutes) {
        return TimeParserUtil.formatWithLocalization(spentMinutes);
    }

    default Long map(String spentTime) {
        return TimeParserUtil.parseToMinutes(spentTime);
    }

    default EnumDto enumToDto(WorkLogStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
