package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.dtos.project.ProjectRequestDto;
import kg.inai.taskmanager.dtos.project.ProjectResponseDto;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "imageUrl", expression = "java(minioService.getPublicUrl(project.getImagePath()))")
    ProjectResponseDto toDto(Project project, MinioService minioService);
    Project toEntity(ProjectRequestDto projectRequest);

    default EnumDto toDto(ProjectStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
