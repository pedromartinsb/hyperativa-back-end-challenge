package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(final String username, final String email, final String password, final String roles) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        final var encodedPassword = passwordEncoder.encode(password);
        final var user = new User(username, email, encodedPassword, true, roles);
        return userRepository.save(user);
    }
}
