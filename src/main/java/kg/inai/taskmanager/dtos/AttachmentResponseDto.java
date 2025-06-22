package kg.inai.taskmanager.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AttachmentResponseDto(Long id,
                                    String originalFileName,
                                    String downloadUrl,
                                    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                    LocalDateTime uploadedAt) {
}
