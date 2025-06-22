package kg.inai.taskmanager.dtos.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.user.UserShortResponseDto;

import java.time.LocalDateTime;

public record CommentResponseDto(Long id,
                                 String text,
                                 UserShortResponseDto user,
                                 EnumDto status,
                                 @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                 LocalDateTime createdAt,
                                 @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                 LocalDateTime updatedAt) {
}
