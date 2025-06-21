package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.dtos.project.ProjectRequest;
import kg.inai.taskmanager.dtos.project.ProjectResponse;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "imageUrl", expression = "java(minioService.getPublicUrl(project.getImagePath()))")
    ProjectResponse toDto(Project project, MinioService minioService);
    Project toEntity(ProjectRequest projectRequest);

    default EnumDto toDto(ProjectStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
