package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.CreditCardRequest;
import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.application.usecase.SaveCreditCardUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hyperativa.visa.adapter.mapper.CreditCardMapper.toResponse;

@RestController
@RequestMapping("/api/v1/cards")
public class CreditCardController {

    private final SaveCreditCardUseCase saveCreditCardUseCase;

    public CreditCardController(SaveCreditCardUseCase saveCreditCardUseCase) {
        this.saveCreditCardUseCase = saveCreditCardUseCase;
    }

    @PostMapping
    public ResponseEntity<CreditCardResponse> saveCard(@Valid @RequestBody CreditCardRequest request) {
        try {
            final var card = saveCreditCardUseCase.execute(
                    request.cardHolder(),
                    request.cardNumber(),
                    request.expirationDate()
            );
            final var response = toResponse(card);
            return ResponseEntity.ok(response);

        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
