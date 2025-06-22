package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.AttachmentResponseDto;
import kg.inai.taskmanager.entities.Attachment;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.AttachmentMapper;
import kg.inai.taskmanager.repositories.AttachmentRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.services.AttachmentService;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final AttachmentMapper attachmentMapper;
    private final MinioService minioService;
    private final AuthService authService;

    @Override
    public List<AttachmentResponseDto> getAll(String taskId) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return task.getAttachments().stream()
                .map(this::mapToDtoWithDownloadUrl)
                .toList();

    }

    @Override
    public void save(String taskId, List<MultipartFile> files) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        save(task, files);
    }

    @Override
    public void save(Task task, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String path = minioService.save(file, null, FileType.TASK_ATTACHMENT.getPath());

            attachmentRepository.save(Attachment.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storagePath(path)
                    .contentType(file.getContentType())
                    .task(task)
                    .user(authService.getAuthenticatedUser())
                    .build());
        }
    }

    private AttachmentResponseDto mapToDtoWithDownloadUrl(Attachment attachment) {
        String downloadUrl = minioService.getDownloadUrl(
                attachment.getStoragePath(),
                attachment.getContentType(),
                attachment.getOriginalFileName()
        );

        return attachmentMapper.toDto(attachment, downloadUrl);
    }
}
