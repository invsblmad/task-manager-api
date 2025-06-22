package kg.inai.taskmanager.services;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.dtos.auth.SignInRequestDto;
import kg.inai.taskmanager.dtos.auth.SignUpRequestDto;
import kg.inai.taskmanager.dtos.auth.TokenResponseDto;

public interface AuthService {
    TokenResponseDto signIn(SignInRequestDto signInRequest);
    TokenResponseDto signUp(SignUpRequestDto signUpRequest);
    User getAuthenticatedUser();
}
