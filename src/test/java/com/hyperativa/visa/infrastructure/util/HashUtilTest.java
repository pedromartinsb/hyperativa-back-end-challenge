package com.hyperativa.visa.infrastructure.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest {

    @Test
    void shouldGenerateValidSha256Hash() {
        // Given
        String input = "test123";
        
        // When
        String hash = HashUtil.sha256(input);
        
        // Then
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 produces 64 hexadecimal characters
        assertTrue(hash.matches("[0-9a-f]+")); // Should only contain hexadecimal characters
    }

    @Test
    void shouldGenerateConsistentHashForSameInput() {
        // Given
        String input = "test123";
        
        // When
        String hash1 = HashUtil.sha256(input);
        String hash2 = HashUtil.sha256(input);
        
        // Then
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldGenerateDifferentHashesForDifferentInputs() {
        // Given
        String input1 = "test123";
        String input2 = "test124";
        
        // When
        String hash1 = HashUtil.sha256(input1);
        String hash2 = HashUtil.sha256(input2);
        
        // Then
        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldHandleEmptyString() {
        // Given
        String input = "";
        
        // When
        String hash = HashUtil.sha256(input);
        
        // Then
        assertNotNull(hash);
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]+"));
    }

    @Test
    void shouldHandleNullInput() {
        // Given
        String input = null;
        
        // When & Then
        assertThrows(NullPointerException.class, () -> HashUtil.sha256(input));
    }

    @Test
    void shouldHandleSpecialCharacters() {
        // Given
        String input = "!@#$%^&*()_+";
        
        // When
        String hash = HashUtil.sha256(input);
        
        // Then
        assertNotNull(hash);
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]+"));
    }
} 