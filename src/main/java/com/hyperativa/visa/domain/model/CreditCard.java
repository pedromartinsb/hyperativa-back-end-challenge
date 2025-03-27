package com.hyperativa.visa.domain.model;

public class CreditCard {
    private String id;
    private String cardHolder;
    private String encryptedCardNumber;
    private String expirationData;

    public CreditCard(String id, String cardHolder, String encryptedCardNumber, String expirationData) {
        this.id = id;
        this.cardHolder = cardHolder;
        this.encryptedCardNumber = encryptedCardNumber;
        this.expirationData = expirationData;
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

    public String getExpirationData() {
        return expirationData;
    }

    public void setExpirationData(String expirationData) {
        this.expirationData = expirationData;
    }
}
