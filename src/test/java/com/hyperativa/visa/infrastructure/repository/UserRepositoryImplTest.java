package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.infrastructure.mapper.UserEntityMapper;
import com.hyperativa.visa.infrastructure.repository.jpa.UserEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private static final String USER_ID = "user-id";
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ROLES = "ROLE_USER";

    private User domainUser;
    private UserEntity entityUser;

    @BeforeEach
    void setUp() {
        domainUser = new User(USERNAME, EMAIL, PASSWORD, true, ROLES);
        domainUser.setId(USER_ID);

        entityUser = new UserEntity();
        entityUser.setId(USER_ID);
        entityUser.setUsername(USERNAME);
        entityUser.setEmail(EMAIL);
        entityUser.setPassword(PASSWORD);
        entityUser.setEnabled(true);
        entityUser.setRoles(ROLES);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Given
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(entityUser);

        // When
        User result = userRepository.save(domainUser);

        // Then
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertTrue(result.isEnabled());
        assertEquals(ROLES, result.getRoles());

        verify(userJpaRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByUsername() {
        // Given
        when(userJpaRepository.findByUsername(USERNAME)).thenReturn(Optional.of(entityUser));

        // When
        Optional<User> result = userRepository.findByUsername(USERNAME);

        // Then
        assertTrue(result.isPresent());
        User user = result.get();
        assertEquals(USER_ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertTrue(user.isEnabled());
        assertEquals(ROLES, user.getRoles());

        verify(userJpaRepository).findByUsername(USERNAME);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Given
        when(userJpaRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByUsername(USERNAME);

        // Then
        assertTrue(result.isEmpty());
        verify(userJpaRepository).findByUsername(USERNAME);
    }
} 