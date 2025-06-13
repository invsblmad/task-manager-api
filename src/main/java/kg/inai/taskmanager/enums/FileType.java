package kg.inai.taskmanager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    AVATAR("avatars"),
    TASK_ATTACHMENT("task-attachments"),
    ;

    private final String path;
}
