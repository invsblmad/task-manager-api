package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.AttachmentResponseDto;
import kg.inai.taskmanager.entities.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    @Mapping(target = "downloadUrl", source = "downloadUrl")
    @Mapping(target = "uploadedAt", source = "attachment.createdAt")
    AttachmentResponseDto toDto(Attachment attachment, String downloadUrl);
}
