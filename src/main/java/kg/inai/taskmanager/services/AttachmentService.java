package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.AttachmentResponseDto;
import kg.inai.taskmanager.entities.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    List<AttachmentResponseDto> getAll(String taskId);

    void save(String taskId, List<MultipartFile> files);

    void save(Task task, List<MultipartFile> files);
}
