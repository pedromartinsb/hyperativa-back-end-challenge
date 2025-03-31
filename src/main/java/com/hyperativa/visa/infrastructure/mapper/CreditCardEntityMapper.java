package com.hyperativa.visa.infrastructure.mapper;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.UserEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CreditCardEntityMapper {

    private final UserJpaRepository userJpaRepository;

    public CreditCardEntityMapper(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public CreditCard toDomain(final CreditCardEntity entity) {
        if (entity == null) {
            return null;
        }
        final var domain = new CreditCard(
                entity.getCardHolder(),
                entity.getEncryptedCardNumber(),
                entity.getExpirationDate(),
                entity.getCardNumberHash(),
                UserEntityMapper.toDomain(entity.getUser())
        );
        domain.setId(entity.getId());
        return domain;
    }

    public CreditCardEntity toEntity(final CreditCard domain) {
        final var entity = new CreditCardEntity();
        entity.setCardHolder(domain.getCardHolder());
        entity.setEncryptedCardNumber(domain.getEncryptedCardNumber());
        entity.setExpirationDate(domain.getExpirationDate());
        entity.setCardNumberHash(domain.getCardNumberHash());

        final var userEntity = userJpaRepository.findById(domain.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found in database"));
        entity.setUser(userEntity);
        
        return entity;
    }
}