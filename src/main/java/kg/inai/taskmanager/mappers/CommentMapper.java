package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.comment.CommentRequestDto;
import kg.inai.taskmanager.dtos.comment.CommentResponseDto;
import kg.inai.taskmanager.entities.Comment;
import kg.inai.taskmanager.enums.CommentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toDto(Comment comment);
    Comment toEntity(CommentRequestDto commentRequest);

    default EnumDto enumToDto(CommentStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
