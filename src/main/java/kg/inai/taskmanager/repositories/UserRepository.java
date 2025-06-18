package kg.inai.taskmanager.repositories;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(UserStatus status);
}
