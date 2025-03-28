package com.hyperativa.visa.adapter.mapper;

import com.hyperativa.visa.adapter.exception.IllegalCardRequestFormatException;
import com.hyperativa.visa.adapter.request.CreditCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextCreditCardMapper {

    private static final Logger logger = LoggerFactory.getLogger(TextCreditCardMapper.class);

    public static CreditCardRequest fromText(final String text) {
        String[] parts = text.split(",");
        if (parts.length != 3) {
            logger.error("Formato inválido. Deve conter 3 partes separadas por vírgula: cardHolder,cardNumber,expirationDate");
            throw new IllegalCardRequestFormatException("Formato inválido. Deve conter 3 partes separadas por vírgula: cardHolder,cardNumber,expirationDate");
        }

        return new CreditCardRequest(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
