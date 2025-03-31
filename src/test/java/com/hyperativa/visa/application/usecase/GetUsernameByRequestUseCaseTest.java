package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.config.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsernameByRequestUseCaseTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GetUsernameByRequestUseCase getUsernameByRequestUseCase;

    private static final String USERNAME = "testuser";
    private static final String TOKEN = "valid.jwt.token";

    @Test
    void shouldExtractUsernameFromValidToken() {
        // Given
        when(jwtUtils.resolveToken(request)).thenReturn(TOKEN);
        when(jwtUtils.validateJwtToken(TOKEN)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(TOKEN)).thenReturn(USERNAME);

        // When
        String result = getUsernameByRequestUseCase.execute(request);

        // Then
        assertEquals(USERNAME, result);
        verify(jwtUtils).resolveToken(request);
        verify(jwtUtils).validateJwtToken(TOKEN);
        verify(jwtUtils).getUserNameFromJwtToken(TOKEN);
    }

    @Test
    void shouldHandleMissingToken() {
        // Given
        when(jwtUtils.resolveToken(request)).thenReturn(null);

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> getUsernameByRequestUseCase.execute(request));
        assertEquals("JWT token is missing", exception.getMessage());

        verify(jwtUtils).resolveToken(request);
        verifyNoMoreInteractions(jwtUtils);
    }

    @Test
    void shouldHandleInvalidToken() {
        // Given
        when(jwtUtils.resolveToken(request)).thenReturn(TOKEN);
        when(jwtUtils.validateJwtToken(TOKEN)).thenReturn(false);

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> getUsernameByRequestUseCase.execute(request));
        assertEquals("Invalid JWT token", exception.getMessage());

        verify(jwtUtils).resolveToken(request);
        verify(jwtUtils).validateJwtToken(TOKEN);
        verifyNoMoreInteractions(jwtUtils);
    }
} 