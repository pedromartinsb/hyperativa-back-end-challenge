package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.domain.service.CryptoService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveCreditCardUseCaseTest {

    @Mock
    private CreditCardRepositoryPort creditCardRepositoryPort;

    @Mock
    private CryptoService cryptoService;

    @Mock
    private GetUsernameByRequestUseCase getUsernameByRequestUseCase;

    @Mock
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SaveCreditCardUseCase saveCreditCardUseCase;

    private static final String CARD_HOLDER = "John Doe";
    private static final String CARD_NUMBER = "4111111111111111";
    private static final String EXPIRATION_DATE = "12/25";
    private static final String USERNAME = "testuser";
    private static final String ENCRYPTED_NUMBER = "encrypted-number";
    private static final String CARD_NUMBER_HASH = "card-number-hash";

    private User testUser;
    private CreditCard testCreditCard;

    @BeforeEach
    void setUp() {
        testUser = new User(USERNAME, "test@example.com", "password", true, "ROLE_USER");
        testUser.setId("user-id");

        testCreditCard = new CreditCard(CARD_HOLDER, ENCRYPTED_NUMBER, EXPIRATION_DATE, CARD_NUMBER_HASH, testUser);
        testCreditCard.setId("card-id");

        when(getUsernameByRequestUseCase.execute(request)).thenReturn(USERNAME);
    }

    @Test
    void shouldSaveCreditCardSuccessfully() {
        // Given
        when(findUserByUsernameUseCase.execute(USERNAME)).thenReturn(testUser);
        when(cryptoService.encrypt(CARD_NUMBER)).thenReturn(ENCRYPTED_NUMBER);
        when(creditCardRepositoryPort.save(any(CreditCard.class))).thenReturn(testCreditCard);

        // When
        CreditCard result = saveCreditCardUseCase.execute(CARD_HOLDER, CARD_NUMBER, EXPIRATION_DATE, request);

        // Then
        assertNotNull(result);
        assertEquals("card-id", result.getId());
        assertEquals(CARD_HOLDER, result.getCardHolder());
        assertEquals(ENCRYPTED_NUMBER, result.getEncryptedCardNumber());
        assertEquals(EXPIRATION_DATE, result.getExpirationDate());
        assertEquals(testUser, result.getUser());

        verify(getUsernameByRequestUseCase).execute(request);
        verify(findUserByUsernameUseCase).execute(USERNAME);
        verify(cryptoService).encrypt(CARD_NUMBER);
        verify(creditCardRepositoryPort).save(any(CreditCard.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(findUserByUsernameUseCase.execute(USERNAME))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> saveCreditCardUseCase.execute(CARD_HOLDER, CARD_NUMBER, EXPIRATION_DATE, request));
        assertEquals("User not found", exception.getMessage());

        verify(getUsernameByRequestUseCase).execute(request);
        verify(findUserByUsernameUseCase).execute(USERNAME);
        verifyNoInteractions(cryptoService);
        verifyNoInteractions(creditCardRepositoryPort);
    }
} 