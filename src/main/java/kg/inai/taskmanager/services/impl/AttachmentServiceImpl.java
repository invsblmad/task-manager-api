package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.entities.Attachment;
import kg.inai.taskmanager.mappers.AttachmentMapper;
import kg.inai.taskmanager.model.AttachmentModel;
import kg.inai.taskmanager.repositories.AttachmentRepository;
import kg.inai.taskmanager.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public Attachment save(AttachmentModel attachmentModel) {
        Attachment attachment = attachmentMapper.toEntity(attachmentModel);
        return attachmentRepository.save(attachment);
    }
}
