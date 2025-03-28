package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(final String username, final String email, final String password, final String roles) {
        final var encodedPassword = passwordEncoder.encode(password);
        final var user = new User(username, email, encodedPassword, true, roles);
        return userRepository.save(user);
    }
}
