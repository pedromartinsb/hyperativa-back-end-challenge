package com.hyperativa.visa.domain.model;

public class CreditCard {
    private String id;
    private String cardHolder;
    private String encryptedCardNumber;
    private String expirationDate;

    public CreditCard(String cardHolder, String encryptedCardNumber, String expirationDate) {
        this.cardHolder = cardHolder;
        this.encryptedCardNumber = encryptedCardNumber;
        this.expirationDate = expirationDate;
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
}
