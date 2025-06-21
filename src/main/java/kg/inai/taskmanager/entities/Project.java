package kg.inai.taskmanager.entities;

import jakarta.persistence.*;
import kg.inai.taskmanager.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String code;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
