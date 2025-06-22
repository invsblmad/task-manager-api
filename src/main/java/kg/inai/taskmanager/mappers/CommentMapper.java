package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.comment.CommentRequestDto;
import kg.inai.taskmanager.dtos.comment.CommentResponseDto;
import kg.inai.taskmanager.entities.Comment;
import kg.inai.taskmanager.enums.CommentStatus;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "user", expression = "java(userMapper.toShortDto(comment.getUser(), minioService))")
    CommentResponseDto toDto(Comment comment,
                             @Context UserMapper userMapper,
                             @Context MinioService minioService);
    Comment toEntity(CommentRequestDto commentRequest);

    default EnumDto enumToDto(CommentStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
