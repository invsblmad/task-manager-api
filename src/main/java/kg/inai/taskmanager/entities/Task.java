package kg.inai.taskmanager.entities;

import jakarta.persistence.*;
import kg.inai.taskmanager.enums.TaskPriority;
import kg.inai.taskmanager.enums.TaskStatus;
import kg.inai.taskmanager.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @EmbeddedId
    private TaskId id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "parent_project_code", referencedColumnName = "projectCode"),
            @JoinColumn(name = "parent_sequence_number", referencedColumnName = "sequenceNumber")
    })
    private Task parentTask;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private Long estimateMinutes;

    @Column(nullable = false)
    private Long remainingMinutes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
