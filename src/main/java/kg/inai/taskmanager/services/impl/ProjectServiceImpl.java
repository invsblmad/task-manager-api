package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.project.ProjectRequestDto;
import kg.inai.taskmanager.dtos.project.ProjectResponseDto;
import kg.inai.taskmanager.dtos.project.ProjectUpdateRequestDto;
import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.enums.ProjectStatus;
import kg.inai.taskmanager.exceptions.AlreadyExistsException;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.ProjectMapper;
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
    public Page<ProjectResponseDto> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(project -> projectMapper.toDto(project, minioService));

    }

    @Override
    public List<ProjectResponseDto> getAllActive() {
        return projectRepository.findAllByStatus(ProjectStatus.ACTIVE).stream()
                .map(project -> projectMapper.toDto(project, minioService))
                .toList();
    }

    @Override
    public ProjectResponseDto getByCode(String code) {
        Project project = projectRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        return projectMapper.toDto(project, minioService);
    }

    @Override
    public ProjectResponseDto save(ProjectRequestDto request, MultipartFile image) {
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
    public void update(String code, ProjectUpdateRequestDto request, MultipartFile image) {
        Project project = projectRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        if (request != null) {
            project.setName(request.name());
        }

        if (image != null && !image.isEmpty()) {
            String imagePath = minioService.save(
                    image,
                    project.getImagePath(),
                    FileType.PROJECT_IMAGE.getPath());
            project.setImagePath(imagePath);
        }

        projectRepository.save(project);
    }

    @Override
    public void updateStatus(String code, ProjectStatus status) {
        Project project = projectRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Проект не найден"));

        project.setStatus(status);
        projectRepository.save(project);
    }
}
