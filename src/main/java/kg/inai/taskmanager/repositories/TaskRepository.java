package kg.inai.taskmanager.repositories;

import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.TaskId;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, TaskId> {

    @Query("SELECT MAX(t.id.sequenceNumber) FROM Task t WHERE t.id.projectCode = :projectCode")
    Long findMaxSequenceNumberByProjectCode(@Param("projectCode") String projectCode);

    List<Task> findAllByIdProjectCodeAndStatus(String projectCode, TaskStatus status);

    List<Task> findAllByIdProjectCodeAndStatusAndAssignedTo(String projectCode, TaskStatus status, User user);

}
