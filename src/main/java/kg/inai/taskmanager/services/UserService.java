package kg.inai.taskmanager.services;

import kg.inai.taskmanager.models.user.UserResponse;

public interface UserService {
    UserResponse getAuthenticatedUser();
}
