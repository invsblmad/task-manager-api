package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.exceptions.PasswordNotConfirmedException;
import kg.inai.taskmanager.exceptions.UserAlreadyExistsException;
import kg.inai.taskmanager.mappers.UserMapper;
import kg.inai.taskmanager.models.auth.SignInRequest;
import kg.inai.taskmanager.models.auth.SignUpRequest;
import kg.inai.taskmanager.models.auth.TokenResponse;
import kg.inai.taskmanager.repositories.UserRepository;
import kg.inai.taskmanager.security.jwt.JwtTokenService;
import kg.inai.taskmanager.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.email(),
                        signInRequest.password()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenService.generateTokens((UserDetails) authentication.getPrincipal());
    }

    @Override
    public TokenResponse signUp(SignUpRequest signUpRequest) {
        if (!signUpRequest.password().equals(signUpRequest.passwordConfirmation())) {
            throw new PasswordNotConfirmedException("Пароли не совпадают");
        }

        User user = userMapper.toEntity(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        return jwtTokenService.generateTokens(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt token) {
            String username = token.getSubject();
            return (User) userDetailsService.loadUserByUsername(username);
        }
        return null;
    }
}
