package kg.inai.taskmanager.model;

import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentModel {

    private String originalFileName;
    private String storagePath;
    private String contentType;
    private Task task;
    private User user;
}
