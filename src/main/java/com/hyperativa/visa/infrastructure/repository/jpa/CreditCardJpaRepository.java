package com.hyperativa.visa.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardJpaRepository extends JpaRepository<CreditCardEntity, String> {
}
