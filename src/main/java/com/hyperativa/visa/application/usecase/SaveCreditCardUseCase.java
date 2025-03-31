package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.domain.service.CryptoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hyperativa.visa.infrastructure.util.HashUtil.sha256;

@Service
@Transactional
public class SaveCreditCardUseCase {

    private final CreditCardRepositoryPort creditCardRepositoryPort;
    private final CryptoService cryptoService;
    private final GetUsernameByRequestUseCase getUsernameByRequestUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    public SaveCreditCardUseCase(CreditCardRepositoryPort creditCardRepositoryPort,
                                 CryptoService cryptoService,
                                 GetUsernameByRequestUseCase getUsernameByRequestUseCase,
                                 FindUserByUsernameUseCase findUserByUsernameUseCase) {
        this.creditCardRepositoryPort = creditCardRepositoryPort;
        this.cryptoService = cryptoService;
        this.getUsernameByRequestUseCase = getUsernameByRequestUseCase;
        this.findUserByUsernameUseCase = findUserByUsernameUseCase;
    }

    public CreditCard execute(final String cardHolder, final String cardNumber,
                              final String expirationDate, final HttpServletRequest request) {
        final var encryptedNumber = cryptoService.encrypt(cardNumber);
        final var cardNumberHash = sha256(cardNumber);
        final var username = getUsernameByRequestUseCase.execute(request);
        final var user = this.findUserByUsernameUseCase.execute(username);
        
        // Create credit card with the managed user entity
        final var creditCard = new CreditCard(cardHolder, encryptedNumber, expirationDate, cardNumberHash, user);
        return creditCardRepositoryPort.save(creditCard);
    }
}
