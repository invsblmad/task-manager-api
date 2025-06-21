package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.comment.CommentRequest;
import kg.inai.taskmanager.dtos.comment.CommentResponse;
import kg.inai.taskmanager.entities.Comment;
import kg.inai.taskmanager.enums.CommentStatus;
import kg.inai.taskmanager.enums.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponse toDto(Comment comment);
    Comment toEntity(CommentRequest commentRequest);

    default EnumDto enumToDto(CommentStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
