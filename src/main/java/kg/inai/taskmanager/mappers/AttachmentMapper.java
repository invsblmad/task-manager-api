package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.Attachment;
import kg.inai.taskmanager.model.AttachmentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    Attachment toEntity(AttachmentModel attachmentModel);
}
