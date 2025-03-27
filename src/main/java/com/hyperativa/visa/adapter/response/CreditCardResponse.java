package com.hyperativa.visa.adapter.response;

public record CreditCardResponse(String id,
                                 String cardHolder,
                                 String expirationDate,
                                 String maskedCardNumber) {
}
