package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.CreditCardRequest;
import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.adapter.response.IdentifierResponse;
import com.hyperativa.visa.application.usecase.FindCardIdentifierUseCase;
import com.hyperativa.visa.application.usecase.SaveCreditCardUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.hyperativa.visa.adapter.mapper.CreditCardMapper.toResponse;
import static com.hyperativa.visa.adapter.mapper.TextCreditCardMapper.fromText;

@RestController
@RequestMapping("/cards")
public class CreditCardController {

    private final SaveCreditCardUseCase saveCreditCardUseCase;
    private final FindCardIdentifierUseCase findCardIdentifierUseCase;

    public CreditCardController(SaveCreditCardUseCase saveCreditCardUseCase,
                                FindCardIdentifierUseCase findCardIdentifierUseCase) {
        this.saveCreditCardUseCase = saveCreditCardUseCase;
        this.findCardIdentifierUseCase = findCardIdentifierUseCase;
    }

    @GetMapping("/identifier")
    public ResponseEntity<IdentifierResponse> getCardIdentifier(@RequestParam("cardNumber") String cardNumber) {
        try {
            Optional<String> optionalResponse = findCardIdentifierUseCase.execute(cardNumber);
            if (optionalResponse.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            final var response = new IdentifierResponse(optionalResponse.get());
            return ResponseEntity.ok(response);

        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreditCardResponse> saveCard(@Valid @RequestBody CreditCardRequest request,
                                                       UriComponentsBuilder uriBuilder) {
        try {
            final var card = saveCreditCardUseCase.execute(
                    request.cardHolder(),
                    request.cardNumber(),
                    request.expirationDate()
            );
            final var response = toResponse(card);
            URI location = uriBuilder
                    .path("/api/v1/cards/{id}")
                    .buildAndExpand(response.id())
                    .toUri();
            return ResponseEntity.created(location).body(response);

        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<CreditCardResponse> saveCardTxt(@RequestBody String body,
                                                          UriComponentsBuilder uriBuilder) {
        try {
            CreditCardRequest request = fromText(body);
            final var card = saveCreditCardUseCase.execute(
                    request.cardHolder(),
                    request.cardNumber(),
                    request.expirationDate()
            );
            CreditCardResponse response = toResponse(card);
            URI location = uriBuilder
                    .path("/api/v1/cards/{id}")
                    .buildAndExpand(response.id())
                    .toUri();
            return ResponseEntity.created(location).body(response);

        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
