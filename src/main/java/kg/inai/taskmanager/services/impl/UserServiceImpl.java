package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.models.user.UserResponse;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    @Override
    public UserResponse getAuthenticatedUser() {
        return userMapper.toDto(authService.getAuthenticatedUser());
    }
}
