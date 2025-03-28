package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper.toDomain;
import static com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper.toEntity;

@Repository
public class CreditCardRepositoryImpl implements CreditCardRepositoryPort {

    private final CreditCardJpaRepository creditCardJpaRepository;

    @Autowired
    public CreditCardRepositoryImpl(CreditCardJpaRepository creditCardJpaRepository) {
        this.creditCardJpaRepository = creditCardJpaRepository;
    }

    @Override
    public CreditCard save(final CreditCard creditCard) {
        CreditCardEntity entity = toEntity(creditCard);
        CreditCardEntity savedEntity = creditCardJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<CreditCard> findByCardNumberHash(final String cardNumberHash) {
        return creditCardJpaRepository.findByCardNumberHash(cardNumberHash)
                .map(CreditCardEntityMapper::toDomain);
    }
}
