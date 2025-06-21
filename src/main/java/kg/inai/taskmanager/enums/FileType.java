package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    AVATAR("avatars"),
    TASK_ATTACHMENT("task-attachments"),
    PROJECT_IMAGE("project-images")
    ;

    private final String path;
}
