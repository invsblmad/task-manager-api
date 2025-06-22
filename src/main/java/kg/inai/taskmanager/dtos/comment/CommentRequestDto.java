package kg.inai.taskmanager.dtos.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(
        @NotBlank(message = "Текст комментария не может быть пустым")
        String text) {
}
