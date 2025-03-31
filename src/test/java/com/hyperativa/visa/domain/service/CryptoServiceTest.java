package com.hyperativa.visa.domain.service;

import com.hyperativa.visa.infrastructure.service.CryptoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTest {

    private CryptoService cryptoService;
    private static final String TEST_KEY = "1234567890123456"; // 16 bytes for AES-128
    private static final String TEST_DATA = "4111111111111111";

    @BeforeEach
    void setUp() {
        cryptoService = new CryptoServiceImpl();
        // Set the crypto key using reflection since it's @Value injected
        try {
            java.lang.reflect.Field field = CryptoServiceImpl.class.getDeclaredField("cryptoKey");
            field.setAccessible(true);
            field.set(cryptoService, TEST_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set crypto key", e);
        }
    }

    @Test
    void shouldEncryptData() {
        // When
        String encrypted = cryptoService.encrypt(TEST_DATA);

        // Then
        assertNotNull(encrypted);
        assertNotEquals(TEST_DATA, encrypted);
        assertTrue(encrypted.length() > 0);
    }

    @Test
    void shouldHandleEmptyString() {
        // When
        String encrypted = cryptoService.encrypt("");

        // Then
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > 0);
    }

    @Test
    void shouldHandleNullInput() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cryptoService.encrypt(null));
        assertEquals("Data to encrypt cannot be null", exception.getMessage());
    }
} 