package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.domain.service.CryptoService;
import org.springframework.stereotype.Service;

@Service
public class SaveCreditCardUseCase {

    private final CreditCardRepositoryPort creditCardRepositoryPort;
    private final CryptoService cryptoService;

    public SaveCreditCardUseCase(CreditCardRepositoryPort creditCardRepositoryPort,
                                 CryptoService cryptoService) {
        this.creditCardRepositoryPort = creditCardRepositoryPort;
        this.cryptoService = cryptoService;
    }

    public CreditCard execute(final String cardHolder, final String cardNumber, final String expirationDate) throws Exception {
        final var encryptedNumber = cryptoService.encrypt(cardNumber);
        final var creditCard = new CreditCard(cardHolder, encryptedNumber, expirationDate);
        return creditCardRepositoryPort.save(creditCard);
    }
}
