package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.user.UserDetailedResponse;
import kg.inai.taskmanager.dtos.user.UserResponse;
import kg.inai.taskmanager.dtos.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Page<UserResponse> getAll(Pageable pageable);

    List<UserResponse> getAllActive();

    UserDetailedResponse getById(Long id);

    UserDetailedResponse getAuthenticatedUser();

    void block(Long id);

    void update(Long id, UserUpdateRequest request, MultipartFile avatar);

    List<UserResponse> getByTeam(Long teamId);

    void addToTeam(Long userId, Long teamId);

    void deleteFromTeam(Long userId);
}
