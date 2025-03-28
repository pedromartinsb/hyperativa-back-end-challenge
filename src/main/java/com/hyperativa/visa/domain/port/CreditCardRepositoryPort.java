package com.hyperativa.visa.domain.port;

import com.hyperativa.visa.domain.model.CreditCard;

import java.util.Optional;

public interface CreditCardRepositoryPort {
    CreditCard save(CreditCard creditCard);

    Optional<CreditCard> findByCardNumberHash(String cardNumberHash);
}
