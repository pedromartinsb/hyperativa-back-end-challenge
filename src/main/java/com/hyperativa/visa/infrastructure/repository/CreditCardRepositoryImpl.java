package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CreditCardRepositoryImpl implements CreditCardRepositoryPort {

    private final CreditCardJpaRepository creditCardJpaRepository;

    @Autowired
    public CreditCardRepositoryImpl(CreditCardJpaRepository creditCardJpaRepository) {
        this.creditCardJpaRepository = creditCardJpaRepository;
    }

    @Override
    public CreditCard save(final CreditCard creditCard) {
        final var entity = new CreditCardEntity();
        entity.setCardHolder(creditCard.getCardHolder());
        entity.setEncryptedCardNumber(creditCard.getEncryptedCardNumber());
        entity.setExpirationDate(creditCard.getExpirationDate());

        final var savedEntity = creditCardJpaRepository.save(entity);
        creditCard.setId(savedEntity.getId());
        return creditCard;
    }
}
