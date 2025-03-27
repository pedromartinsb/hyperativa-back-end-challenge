package com.hyperativa.visa.adapter.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreditCardRequest(
        @NotBlank(message = "O nome do titular é obrigatório.")
        String cardHolder,
        @NotBlank(message = "O número do cartão é obrigatório.")
        @Size(min = 13, max = 19, message = "O número do cartão deve ter entre 13 e 19 dígitos.")
        @Pattern(regexp = "\\d+", message = "O número do cartão deve conter apenas dígitos.")
        String cardNumber,
        @NotBlank(message = "A data de expiração é obrigatória.")
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2,4}$", message = "A data de expiração deve estar no formato MM/YY ou MM/YYYY.")
        String expirationDate
) {}
