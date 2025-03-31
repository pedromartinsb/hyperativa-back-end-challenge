package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByUsernameUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    private static final String USER_ID = "user-id";
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ROLES = "ROLE_USER";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, EMAIL, PASSWORD, true, ROLES);
        user.setId(USER_ID);
    }

    @Test
    void shouldFindUserByUsernameSuccessfully() {
        // Given
        when(userRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        // When
        User result = findUserByUsernameUseCase.execute(USERNAME);

        // Then
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertTrue(result.isEnabled());
        assertEquals(ROLES, result.getRoles());

        verify(userRepositoryPort).findByUsername(USERNAME);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> findUserByUsernameUseCase.execute(USERNAME));
        assertEquals("Usuário não encontrado pelo username", exception.getMessage());

        verify(userRepositoryPort).findByUsername(USERNAME);
    }

    @Test
    void shouldHandleNullUsername() {
        // Given
        String nullUsername = null;

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> findUserByUsernameUseCase.execute(nullUsername));
        assertEquals("Usuário não encontrado pelo username", exception.getMessage());

        verify(userRepositoryPort).findByUsername(nullUsername);
    }
} 