package com.hyperativa.visa.adapter.mapper;

import com.hyperativa.visa.adapter.request.CreditCardRequest;

public class TextCreditCardMapper {

    public static CreditCardRequest fromText(final String text) {
        String[] parts = text.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato inválido. Deve conter 3 partes separadas por vírgula: cardHolder,cardNumber,expirationDate");
        }

        return new CreditCardRequest(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
