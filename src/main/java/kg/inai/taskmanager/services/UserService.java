package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.user.UserDetailedResponseDto;
import kg.inai.taskmanager.dtos.user.UserResponseDto;
import kg.inai.taskmanager.dtos.user.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Page<UserResponseDto> getAll(Pageable pageable);

    List<UserResponseDto> getAllActive();

    UserDetailedResponseDto getById(Long id);

    UserDetailedResponseDto getAuthenticatedUser();

    void block(Long id);

    void update(Long id, UserUpdateRequestDto request, MultipartFile avatar);

    List<UserResponseDto> getByTeam(Long teamId);

    void addToTeam(Long userId, Long teamId);

    void deleteFromTeam(Long userId);
}
