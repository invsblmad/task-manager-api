package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.project.ProjectRequest;
import kg.inai.taskmanager.dtos.project.ProjectResponse;
import kg.inai.taskmanager.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    Page<ProjectResponse> getAll(Pageable pageable);

    List<ProjectResponse> getAllActive();

    ProjectResponse getByCode(String code);

    ProjectResponse save(ProjectRequest request, MultipartFile image);

    void update(String code, ProjectRequest request, MultipartFile image);

    void updateStatus(String code, ProjectStatus status);
}
