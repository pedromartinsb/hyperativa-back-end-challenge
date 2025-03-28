package com.hyperativa.visa.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardJpaRepository extends JpaRepository<CreditCardEntity, String> {
    Optional<CreditCardEntity> findByCardNumberHash(String cardNumberHash);
}
