package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.exceptions.AlreadyExistsException;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.ProjectMapper;
import kg.inai.taskmanager.dtos.project.ProjectRequest;
import kg.inai.taskmanager.dtos.project.ProjectResponse;
import kg.inai.taskmanager.repositories.ProjectRepository;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MinioService minioService;

    @Override
    public Page<ProjectResponse> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(project -> projectMapper.toDto(project, minioService));

    }

    @Override
    public List<ProjectResponse> getAllActive() {
        return projectRepository.findAllByStatus(ProjectStatus.ACTIVE).stream()
                .map(project -> projectMapper.toDto(project, minioService))
                .toList();
    }

    @Override
    public ProjectResponse getById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        return projectMapper.toDto(project, minioService);
    }

    @Override
    public ProjectResponse save(ProjectRequest request, MultipartFile image) {
        Project project = projectMapper.toEntity(request);
        project.setStatus(ProjectStatus.ACTIVE);

        if (image != null && !image.isEmpty()) {
            String imagePath = minioService.save(
                    image,
                    project.getImagePath(),
                    FileType.PROJECT_IMAGE.getPath());
            project.setImagePath(imagePath);
        }

        try {
            project = projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Проект с таким кодом уже существует");
        }
        return projectMapper.toDto(project, minioService);
    }

    @Override
    public void update(Long id, ProjectRequest request, MultipartFile image) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        if (request != null) {
            project.setCode(request.code());
            project.setName(request.name());
        }

        if (image != null && !image.isEmpty()) {
            String imagePath = minioService.save(
                    image,
                    project.getImagePath(),
                    FileType.PROJECT_IMAGE.getPath());
            project.setImagePath(imagePath);
        }

        try {
            projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Проект с таким кодом уже существует");
        }
    }

    @Override
    public void updateStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        project.setStatus(status);
        projectRepository.save(project);
    }
}
