package kg.inai.taskmanager.repositories;

import kg.inai.taskmanager.entities.Comment;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.enums.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask(Task task);
    List<Comment> findAllByTaskAndStatusNot(Task task, CommentStatus status);
}
