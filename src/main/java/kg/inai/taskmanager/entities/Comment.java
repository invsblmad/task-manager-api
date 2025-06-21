package kg.inai.taskmanager.entities;

import jakarta.persistence.*;
import kg.inai.taskmanager.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "parent_project_code", referencedColumnName = "projectCode"),
            @JoinColumn(name = "parent_sequence_number", referencedColumnName = "sequenceNumber")
    })
    private Task task;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
