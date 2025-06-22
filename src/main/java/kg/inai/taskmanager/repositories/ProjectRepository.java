package kg.inai.taskmanager.repositories;

import kg.inai.taskmanager.entities.Project;
import kg.inai.taskmanager.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByStatus(ProjectStatus status);
    Optional<Project> findByCode(String code);
}
