package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.port.CreditCardRepositoryPort;
import com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.CreditCardJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper.toDomain;
import static com.hyperativa.visa.infrastructure.mapper.CreditCardEntityMapper.toEntity;

@Repository
public class CreditCardRepositoryImpl implements CreditCardRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(CreditCardRepositoryImpl.class);
    private final CreditCardJpaRepository creditCardJpaRepository;

    public CreditCardRepositoryImpl(CreditCardJpaRepository creditCardJpaRepository) {
        this.creditCardJpaRepository = creditCardJpaRepository;
    }

    @Override
    public CreditCard save(final CreditCard creditCard) {
        logger.debug("Iniciando salvamento do cartão para o titular: {}", creditCard.getCardHolder());

        CreditCardEntity entity = toEntity(creditCard);
        logger.debug("Entidade convertida: {}", entity);

        CreditCardEntity savedEntity = creditCardJpaRepository.save(entity);
        logger.info("Cartão salvo com sucesso com ID: {}", savedEntity.getId());

        CreditCard domainCreditCard = toDomain(savedEntity);
        logger.debug("Entidade convertida para domínio: {}", domainCreditCard);
        return domainCreditCard;
    }

    @Override
    public Optional<CreditCard> findByCardNumberHash(final String cardNumberHash) {
        logger.debug("Buscando cartão com hash: {}", cardNumberHash);
        Optional<CreditCard> result = creditCardJpaRepository.findByCardNumberHash(cardNumberHash)
                .map(CreditCardEntityMapper::toDomain);
        if (result.isPresent()) {
            logger.info("Cartão encontrado com ID: {}", result.get().getId());
        } else {
            logger.warn("Nenhum cartão encontrado para o hash: {}", cardNumberHash);
        }
        return result;
    }
}
