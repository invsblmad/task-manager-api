package kg.inai.taskmanager.model;

import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.entities.WorkLog;
import kg.inai.taskmanager.enums.WorkLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    List<WorkLog> findAllByTaskAndStatusNot(Task task, WorkLogStatus status);

    List<WorkLog> findAllByTask(Task task);
}
