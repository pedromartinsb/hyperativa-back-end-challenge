package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.CreditCardRequest;
import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.adapter.response.IdentifierResponse;
import com.hyperativa.visa.application.usecase.FindCardIdentifierUseCase;
import com.hyperativa.visa.application.usecase.SaveCreditCardUseCase;
import com.hyperativa.visa.domain.exception.CreditCardNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.hyperativa.visa.adapter.mapper.CreditCardMapper.maskCardNumber;
import static com.hyperativa.visa.adapter.mapper.CreditCardMapper.toResponse;
import static com.hyperativa.visa.adapter.mapper.TextCreditCardMapper.fromText;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    private static final Logger logger = LoggerFactory.getLogger(CreditCardController.class);
    private final SaveCreditCardUseCase saveCreditCardUseCase;
    private final FindCardIdentifierUseCase findCardIdentifierUseCase;

    public CreditCardController(SaveCreditCardUseCase saveCreditCardUseCase, FindCardIdentifierUseCase findCardIdentifierUseCase) {
        this.saveCreditCardUseCase = saveCreditCardUseCase;
        this.findCardIdentifierUseCase = findCardIdentifierUseCase;
    }

    @GetMapping("/identifier")
    public ResponseEntity<IdentifierResponse> getCardIdentifier(@RequestParam("cardNumber") String cardNumber) {
        logger.info("Recebida requisição para buscar identificador do cartão para número: {}", maskCardNumber(cardNumber));

        Optional<String> optionalResponse = findCardIdentifierUseCase.execute(cardNumber);
        if (optionalResponse.isEmpty()) {
            logger.warn("Cartão não encontrado para o número: {}", maskCardNumber(cardNumber));
            throw new CreditCardNotFoundException("Cartão não encontrado para o número fornecido: " + maskCardNumber(cardNumber));
        }

        logger.info("Cartão encontrado com ID: {}", optionalResponse.get());
        final var response = new IdentifierResponse(optionalResponse.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreditCardResponse> saveCard(@Valid @RequestBody CreditCardRequest creditCardRequest,
                                                       UriComponentsBuilder uriBuilder,
                                                       HttpServletRequest request) {
        logger.info("Recebida requisição para salvar informações do cartão para número: {}", maskCardNumber(creditCardRequest.cardNumber()));
        final var card = saveCreditCardUseCase.execute(
                creditCardRequest.cardHolder(),
                creditCardRequest.cardNumber(),
                creditCardRequest.expirationDate(),
                request
        );
        logger.info("Cartão salvo com ID: {}", card.getId());

        final var response = toResponse(card);
        URI location = uriBuilder
                .path("/api/v1/cards/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<CreditCardResponse> saveCardTxt(@RequestBody String body,
                                                          UriComponentsBuilder uriBuilder,
                                                          HttpServletRequest request) {
        CreditCardRequest creditCardRequest = fromText(body);
        logger.info("Recebida requisição para salvar informações do cartão para número via TXT: {}", maskCardNumber(creditCardRequest.cardNumber()));

        final var card = saveCreditCardUseCase.execute(
                creditCardRequest.cardHolder(),
                creditCardRequest.cardNumber(),
                creditCardRequest.expirationDate(),
                request
        );
        logger.info("Cartão salvo com ID: {}", card.getId());

        CreditCardResponse response = toResponse(card);
        URI location = uriBuilder
                .path("/api/v1/cards/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
