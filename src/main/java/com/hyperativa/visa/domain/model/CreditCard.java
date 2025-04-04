package com.hyperativa.visa.domain.model;

public class CreditCard {
    private String id;
    private String cardHolder;
    private String encryptedCardNumber;
    private String expirationDate;
    private String cardNumberHash;
    private User user;

    public CreditCard(String cardHolder, String encryptedCardNumber, String expirationDate, String cardNumberHash, User user) {
        this.cardHolder = cardHolder;
        this.encryptedCardNumber = encryptedCardNumber;
        this.expirationDate = expirationDate;
        this.cardNumberHash = cardNumberHash;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardNumberHash() {
        return cardNumberHash;
    }

    public void setCardNumberHash(String cardNumberHash) {
        this.cardNumberHash = cardNumberHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
