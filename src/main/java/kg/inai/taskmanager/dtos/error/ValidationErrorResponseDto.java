package kg.inai.taskmanager.dtos.error;

import java.util.Map;

public record ValidationErrorResponseDto(Map<String, String> errors) {
}
