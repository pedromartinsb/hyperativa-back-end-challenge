package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardJpaRepository;
import com.hyperativa.visa.infrastructure.repository.jpa.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardRepositoryImplTest {

    @Mock
    private CreditCardJpaRepository creditCardJpaRepository;

    @Mock
    private CreditCardEntityMapper creditCardEntityMapper;

    @InjectMocks
    private CreditCardRepositoryImpl creditCardRepository;

    private static final String CARD_ID = "card-id";
    private static final String USER_ID = "user-id";
    private static final String CARD_HOLDER = "John Doe";
    private static final String ENCRYPTED_NUMBER = "encrypted-number";
    private static final String EXPIRATION_DATE = "12/25";
    private static final String CARD_NUMBER_HASH = "card-number-hash";

    private CreditCard domainCreditCard;
    private CreditCardEntity entityCreditCard;
    private User domainUser;
    private UserEntity entityUser;

    @BeforeEach
    void setUp() {
        domainUser = new User("testuser", "test@example.com", "password", true, "ROLE_USER");
        domainUser.setId(USER_ID);

        domainCreditCard = new CreditCard(CARD_HOLDER, ENCRYPTED_NUMBER, EXPIRATION_DATE, CARD_NUMBER_HASH, domainUser);
        domainCreditCard.setId(CARD_ID);

        entityUser = new UserEntity();
        entityUser.setId(USER_ID);
        entityUser.setUsername("testuser");
        entityUser.setEmail("test@example.com");
        entityUser.setPassword("password");
        entityUser.setEnabled(true);
        entityUser.setRoles("ROLE_USER");

        entityCreditCard = new CreditCardEntity();
        entityCreditCard.setId(CARD_ID);
        entityCreditCard.setCardHolder(CARD_HOLDER);
        entityCreditCard.setEncryptedCardNumber(ENCRYPTED_NUMBER);
        entityCreditCard.setExpirationDate(EXPIRATION_DATE);
        entityCreditCard.setCardNumberHash(CARD_NUMBER_HASH);
        entityCreditCard.setUser(entityUser);
    }

    @Test
    void shouldSaveCreditCardSuccessfully() {
        // Given
        when(creditCardEntityMapper.toEntity(domainCreditCard)).thenReturn(entityCreditCard);
        when(creditCardJpaRepository.save(entityCreditCard)).thenReturn(entityCreditCard);
        when(creditCardEntityMapper.toDomain(entityCreditCard)).thenReturn(domainCreditCard);

        // When
        CreditCard result = creditCardRepository.save(domainCreditCard);

        // Then
        assertNotNull(result);
        assertEquals(CARD_ID, result.getId());
        assertEquals(CARD_HOLDER, result.getCardHolder());
        assertEquals(ENCRYPTED_NUMBER, result.getEncryptedCardNumber());
        assertEquals(EXPIRATION_DATE, result.getExpirationDate());
        assertEquals(CARD_NUMBER_HASH, result.getCardNumberHash());
        assertEquals(domainUser, result.getUser());

        verify(creditCardEntityMapper).toEntity(domainCreditCard);
        verify(creditCardJpaRepository).save(entityCreditCard);
        verify(creditCardEntityMapper).toDomain(entityCreditCard);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(creditCardEntityMapper.toEntity(domainCreditCard))
            .thenThrow(new IllegalStateException("User not found in database"));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> creditCardRepository.save(domainCreditCard));
        assertEquals("User not found in database", exception.getMessage());

        verify(creditCardEntityMapper).toEntity(domainCreditCard);
        verifyNoInteractions(creditCardJpaRepository);
    }
} 