package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hyperativa.visa.infrastructure.util.HashUtil.sha256;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCardIdentifierUseCaseTest {

    @Mock
    private CreditCardRepositoryPort creditCardRepositoryPort;

    @InjectMocks
    private FindCardIdentifierUseCase findCardIdentifierUseCase;

    private static final String CARD_ID = "card-id";
    private static final String CARD_NUMBER = "4111111111111111";
    private static final String CARD_HOLDER = "John Doe";
    private static final String ENCRYPTED_NUMBER = "encrypted-number";
    private static final String EXPIRATION_DATE = "12/25";
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ROLES = "ROLE_USER";

    private CreditCard creditCard;
    private User user;
    private String cardNumberHash;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, EMAIL, PASSWORD, true, ROLES);
        user.setId("user-id");

        cardNumberHash = sha256(CARD_NUMBER);
        creditCard = new CreditCard(CARD_HOLDER, ENCRYPTED_NUMBER, EXPIRATION_DATE, cardNumberHash, user);
        creditCard.setId(CARD_ID);
    }

    @Test
    void shouldFindCardIdentifierSuccessfully() {
        // Given
        when(creditCardRepositoryPort.findByCardNumberHash(cardNumberHash))
            .thenReturn(Optional.of(creditCard));

        // When
        Optional<String> result = findCardIdentifierUseCase.execute(CARD_NUMBER);

        // Then
        assertTrue(result.isPresent());
        assertEquals(CARD_ID, result.get());
        verify(creditCardRepositoryPort).findByCardNumberHash(cardNumberHash);
    }

    @Test
    void shouldReturnEmptyWhenCardNotFound() {
        // Given
        when(creditCardRepositoryPort.findByCardNumberHash(cardNumberHash))
            .thenReturn(Optional.empty());

        // When
        Optional<String> result = findCardIdentifierUseCase.execute(CARD_NUMBER);

        // Then
        assertTrue(result.isEmpty());
        verify(creditCardRepositoryPort).findByCardNumberHash(cardNumberHash);
    }

    @Test
    void shouldHandleNullCardNumber() {
        // Given
        String nullCardNumber = null;

        // When
        Optional<String> result = findCardIdentifierUseCase.execute(nullCardNumber);

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(creditCardRepositoryPort);
    }

    @Test
    void shouldHandleEmptyCardNumber() {
        // Given
        String emptyCardNumber = "";
        String emptyHash = sha256(emptyCardNumber);

        // When
        Optional<String> result = findCardIdentifierUseCase.execute(emptyCardNumber);

        // Then
        assertTrue(result.isEmpty());
        verify(creditCardRepositoryPort).findByCardNumberHash(emptyHash);
    }
} 