package kg.inai.taskmanager.dtos.work_log;

import com.fasterxml.jackson.annotation.JsonFormat;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.user.UserShortResponseDto;

import java.time.LocalDateTime;

public record WorkLogResponseDto(Long id,
                                 String spentTime,
                                 String text,
                                 UserShortResponseDto user,
                                 EnumDto status,
                                 @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                 LocalDateTime createdAt,
                                 @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                 LocalDateTime updatedAt) {
}
