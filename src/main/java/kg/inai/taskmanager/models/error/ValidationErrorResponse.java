package kg.inai.taskmanager.models.error;

import java.util.Map;

public record ValidationErrorResponse(Map<String, String> errors) {
}
