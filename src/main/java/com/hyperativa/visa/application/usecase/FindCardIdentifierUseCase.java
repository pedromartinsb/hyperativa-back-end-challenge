package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hyperativa.visa.infrastructure.util.HashUtil.sha256;

@Service
public class FindCardIdentifierUseCase {

    private final CreditCardRepositoryPort creditCardRepositoryPort;

    public FindCardIdentifierUseCase(CreditCardRepositoryPort creditCardRepositoryPort) {
        this.creditCardRepositoryPort = creditCardRepositoryPort;
    }

    public Optional<String> execute(final String cardNumber) throws Exception {
        final var cardNumberHash = sha256(cardNumber);
        final var optionalCreditCard = creditCardRepositoryPort.findByCardNumberHash(cardNumberHash);
        return optionalCreditCard.map(CreditCard::getId);
    }
}
