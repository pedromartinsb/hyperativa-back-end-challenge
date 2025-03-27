package com.hyperativa.visa.domain.port;

import com.hyperativa.visa.domain.model.CreditCard;

public interface CreditCardRepositoryPort {
    CreditCard save(CreditCard creditCard);
}
