package kg.inai.taskmanager.dtos.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.user.UserShortResponse;

import java.time.LocalDateTime;

public record CommentResponse(Long id,
                              String text,
                              UserShortResponse user,
                              EnumDto status,
                              @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                              LocalDateTime createdAt,
                              @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                              LocalDateTime updatedAt) {
}
