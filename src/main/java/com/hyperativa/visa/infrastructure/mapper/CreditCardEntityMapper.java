package com.hyperativa.visa.infrastructure.mapper;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;

public class CreditCardEntityMapper {

    public static CreditCard toDomain(final CreditCardEntity entity) {
        if (entity == null) {
            return null;
        }
        final var domain = new CreditCard(
                entity.getCardHolder(),
                entity.getEncryptedCardNumber(),
                entity.getExpirationDate(),
                entity.getCardNumberHash()
        );
        domain.setId(entity.getId());
        return domain;
    }

    public static CreditCardEntity toEntity(final CreditCard domain) {
        final var entity = new CreditCardEntity();
        entity.setCardHolder(domain.getCardHolder());
        entity.setEncryptedCardNumber(domain.getEncryptedCardNumber());
        entity.setExpirationDate(domain.getExpirationDate());
        entity.setCardNumberHash(domain.getCardNumberHash());
        return entity;
    }
}