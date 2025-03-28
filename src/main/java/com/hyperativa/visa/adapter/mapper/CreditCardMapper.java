package com.hyperativa.visa.adapter.mapper;

import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.domain.model.CreditCard;

public class CreditCardMapper {

    public static CreditCardResponse toResponse(final CreditCard creditCard) {
        return new CreditCardResponse(
                creditCard.getId(),
                creditCard.getCardHolder(),
                creditCard.getExpirationDate(),
                maskCardNumber(creditCard.getEncryptedCardNumber()));
    }

    public static String maskCardNumber(final String cardNumber) {
        if (cardNumber != null && cardNumber.length() > 4) {
            return "****" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
    }
}
