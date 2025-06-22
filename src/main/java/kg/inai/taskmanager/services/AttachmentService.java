package kg.inai.taskmanager.services;

import kg.inai.taskmanager.entities.Attachment;
import kg.inai.taskmanager.model.AttachmentModel;

public interface AttachmentService {

    Attachment save(AttachmentModel attachmentModel);
}
