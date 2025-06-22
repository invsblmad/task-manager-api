package kg.inai.taskmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachments")
public class Attachment extends BaseEntity {

    private String originalFileName;

    private String storagePath;

    private String contentType;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "parent_project_code", referencedColumnName = "projectCode"),
            @JoinColumn(name = "parent_sequence_number", referencedColumnName = "sequenceNumber")
    })
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
