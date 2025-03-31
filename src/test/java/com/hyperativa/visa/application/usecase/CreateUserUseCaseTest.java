package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String ROLES = "ROLE_USER";
    private static final String ENCODED_PASSWORD = "encodedPassword123";

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("test-id");
            return user;
        });

        // When
        User result = createUserUseCase.execute(USERNAME, EMAIL, PASSWORD, ROLES);

        // Then
        assertNotNull(result);
        assertEquals("test-id", result.getId());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(ENCODED_PASSWORD, result.getPassword());
        assertEquals(ROLES, result.getRoles());
        assertTrue(result.isEnabled());

        verify(userRepository).findByUsername(USERNAME);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User()));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createUserUseCase.execute(USERNAME, EMAIL, PASSWORD, ROLES));
        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository).findByUsername(USERNAME);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository);
    }
} 