package kg.inai.taskmanager.services;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.models.auth.SignInRequest;
import kg.inai.taskmanager.models.auth.SignUpRequest;
import kg.inai.taskmanager.models.auth.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse signUp(SignUpRequest signUpRequest);
    User getAuthenticatedUser();
}
