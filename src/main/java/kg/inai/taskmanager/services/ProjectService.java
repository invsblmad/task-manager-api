package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.project.ProjectRequestDto;
import kg.inai.taskmanager.dtos.project.ProjectResponseDto;
import kg.inai.taskmanager.dtos.project.ProjectUpdateRequestDto;
import kg.inai.taskmanager.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    Page<ProjectResponseDto> getAll(Pageable pageable);

    List<ProjectResponseDto> getAllActive();

    ProjectResponseDto getByCode(String code);

    ProjectResponseDto save(ProjectRequestDto request, MultipartFile image);

    void update(String code, ProjectUpdateRequestDto request, MultipartFile image);

    void updateStatus(String code, ProjectStatus status);
}
