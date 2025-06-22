package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.user.UserDetailedResponseDto;
import kg.inai.taskmanager.dtos.user.UserResponseDto;
import kg.inai.taskmanager.dtos.user.UserUpdateRequestDto;
import kg.inai.taskmanager.entities.Team;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.FileType;
import kg.inai.taskmanager.enums.UserStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.repositories.TeamRepository;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final MinioService minioService;

    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> userMapper.toDto(user, minioService));
    }

    @Override
    public List<UserResponseDto> getAllActive() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE).stream()
                .map(user -> userMapper.toDto(user, minioService))
                .toList();
    }

    @Override
    public UserDetailedResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userMapper.toDetailedDto(user, minioService);
    }

    @Override
    public UserDetailedResponseDto getAuthenticatedUser() {
        return userMapper.toDetailedDto(authService.getAuthenticatedUser(), minioService);
    }

    @Override
    public void block(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    @Override
    public void update(Long id, UserUpdateRequestDto request, MultipartFile avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user = userMapper.toUpdatedEntity(request, user);

        if (avatar != null && !avatar.isEmpty()) {
            String avatarPath = minioService.save(
                    avatar,
                    user.getAvatarPath(),
                    FileType.AVATAR.getPath());
            user.setAvatarPath(avatarPath);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> getByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Команда не найдена"));

        return team.getUsers().stream()
                .map(user -> userMapper.toDto(user, minioService))
                .toList();
    }

    @Override
    public void addToTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Команда не найдена"));

        user.setTeam(team);
        userRepository.save(user);
    }

    @Override
    public void deleteFromTeam(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setTeam(null);
        userRepository.save(user);
    }
}
