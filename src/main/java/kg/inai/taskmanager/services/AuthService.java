package kg.inai.taskmanager.services;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.dtos.auth.SignInRequest;
import kg.inai.taskmanager.dtos.auth.SignUpRequest;
import kg.inai.taskmanager.dtos.auth.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse signUp(SignUpRequest signUpRequest);
    User getAuthenticatedUser();
}
