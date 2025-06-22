package kg.inai.taskmanager.dtos.task;

import java.util.List;

public record GeneratedResultResponseDto(String parentTaskTitle,
                                         List<GeneratedSubtaskDto> generatedSubtasks) {
}
