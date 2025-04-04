package com.hyperativa.visa.infrastructure.repository.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "credit_cards")
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "encrypted_card_number")
    private String encryptedCardNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "card_number_hash")
    private String cardNumberHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
