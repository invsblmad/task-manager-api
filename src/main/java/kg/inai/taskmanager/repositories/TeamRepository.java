package kg.inai.taskmanager.repositories;

import kg.inai.taskmanager.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByDeleted(boolean deleted);
}
