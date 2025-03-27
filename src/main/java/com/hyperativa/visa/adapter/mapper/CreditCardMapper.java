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

    public static String maskCardNumber(final String encryptedCardNumber) {
        // Como o número está criptografado, normalmente você não consegue extrair os últimos dígitos.
        // Em um cenário real, você pode armazenar separadamente os últimos 4 dígitos do cartão para exibição.
        // Para fins de exemplo, vamos retornar um valor fixo.
        return "**** **** **** 1234";
    }
}
