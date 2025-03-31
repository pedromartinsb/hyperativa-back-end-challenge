package com.hyperativa.visa.common.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void shouldThrowExceptionWhenInstantiated() throws Exception {
        // Given
        var constructor = Constants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When
        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        // Then
        assertTrue(exception.getCause() instanceof IllegalStateException);
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Test
    void shouldHaveCorrectApiVersion() {
        assertEquals("v1", Constants.API_VERSION);
    }

    @Test
    void shouldHaveCorrectBasePath() {
        // Given
        String expectedPath = "/api/" + Constants.API_VERSION;
        
        // Then
        assertEquals(expectedPath, Constants.BASE_PATH);
    }

    @Test
    void shouldHaveCorrectSecurityConstants() {
        assertEquals("Bearer ", Constants.TOKEN_PREFIX);
        assertEquals("Authorization", Constants.HEADER_STRING);
    }

    @Test
    void shouldHaveCorrectErrorMessages() {
        assertEquals("Ocorreu um erro inesperado. Tente novamente mais tarde.", Constants.INTERNAL_SERVER_ERROR);
        assertEquals("Usuário e/ou Senha estão incorretos.", Constants.INVALID_CREDENTIALS);
    }

    @Test
    void shouldHaveCorrectValidationMessages() {
        assertEquals("Número do cartão inválido", Constants.INVALID_CARD_NUMBER);
        assertEquals("Data de expiração inválida", Constants.INVALID_CARD_EXPIRY);
        assertEquals("CVV inválido", Constants.INVALID_CARD_CVV);
    }
} 