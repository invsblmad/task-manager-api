package kg.inai.taskmanager.dtos.error;

import java.util.Map;

public record ValidationErrorResponse(Map<String, String> errors) {
}
