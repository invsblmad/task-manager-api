package kg.inai.taskmanager.services;

import kg.inai.taskmanager.models.user.UserDetailedResponse;
import kg.inai.taskmanager.models.user.UserResponse;
import kg.inai.taskmanager.models.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<UserResponse> getAll(Pageable pageable);

    UserDetailedResponse getById(Long id);

    UserDetailedResponse getAuthenticatedUser();

    void block(Long id);

    void update(Long id, UserUpdateRequest request, MultipartFile avatar);
}
