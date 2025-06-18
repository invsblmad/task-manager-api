package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.enums.UserStatus;
import kg.inai.taskmanager.exceptions.MinioException;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.models.user.UserDetailedResponse;
import kg.inai.taskmanager.models.user.UserResponse;
import kg.inai.taskmanager.models.user.UserUpdateRequest;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.MinioService;
import kg.inai.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final MinioService minioService;

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> userMapper.toModel(user, minioService));
    }

    @Override
    public UserDetailedResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userMapper.toDetailedModel(user, minioService);
    }

    @Override
    public UserDetailedResponse getAuthenticatedUser() {
        return userMapper.toDetailedModel(authService.getAuthenticatedUser(), minioService);
    }

    @Override
    public void block(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    @Override
    public void update(Long id, UserUpdateRequest request, MultipartFile avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user = userMapper.toUpdatedEntity(request, user);

        if (avatar != null && !avatar.isEmpty()) {
            updateAvatar(user, avatar);
        }
        userRepository.save(user);
    }

    private void updateAvatar(User user, MultipartFile avatar) {
        String oldAvatarPath = user.getAvatarPath();
        if (oldAvatarPath != null) {
            try {
                minioService.deleteFile(oldAvatarPath);
            } catch (MinioException e) {
                log.warn("Не удалось удалить старый аватар {}, причина: {}", oldAvatarPath, e.getMessage());
            }
        }
        String avatarPath = minioService.uploadFile(avatar, FileType.AVATAR.getPath());
        user.setAvatarPath(avatarPath);
    }
}
