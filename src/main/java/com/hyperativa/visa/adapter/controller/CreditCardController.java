package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.exception.IllegalCardRequestFormatException;
import com.hyperativa.visa.adapter.mapper.CreditCardMapper;
import com.hyperativa.visa.adapter.request.CreditCardRequest;
import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.adapter.response.ErrorResponse;
import com.hyperativa.visa.adapter.response.IdentifierResponse;
import com.hyperativa.visa.application.usecase.FindCardIdentifierUseCase;
import com.hyperativa.visa.application.usecase.SaveCreditCardUseCase;
import com.hyperativa.visa.common.exception.CreditCardNotFoundException;
import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/credit-cards")
@RequiredArgsConstructor
@Tag(name = "Credit Cards", description = "API for managing credit card operations")
public class CreditCardController {

    private final SaveCreditCardUseCase saveCreditCardUseCase;
    private final FindCardIdentifierUseCase findCardIdentifierUseCase;

    @Operation(
        summary = "Get card identifier",
        description = "Retrieves the identifier for a given credit card number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Card identifier found successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = IdentifierResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Card not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @GetMapping("/identifier")
    public ResponseEntity<IdentifierResponse> getCardIdentifier(
        @Parameter(description = "Credit card number", required = true)
        @RequestParam String cardNumber
    ) {
        log.info("Received request to get card identifier for card number: {}", CreditCardMapper.maskCardNumber(cardNumber));
        return findCardIdentifierUseCase.execute(cardNumber)
            .map(identifier -> {
                log.info("Card identifier found: {}", identifier);
                return ResponseEntity.ok(new IdentifierResponse(identifier));
            })
            .orElseThrow(() -> {
                String maskedCardNumber = CreditCardMapper.maskCardNumber(cardNumber);
                String message = "Cartão não encontrado para o número fornecido: " + maskedCardNumber;
                log.error("Card not found for number: {}", maskedCardNumber);
                return new CreditCardNotFoundException(message);
            });
    }

    @Operation(
        summary = "Save credit card (JSON)",
        description = "Saves a new credit card with encrypted data using JSON format"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Credit card created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreditCardResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request format or data",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditCardResponse> saveCardJson(
        @Parameter(description = "Credit card data", required = true)
        @Valid @RequestBody CreditCardRequest request,
        @Parameter(hidden = true)
        @AuthenticationPrincipal User user,
        @Parameter(hidden = true)
        HttpServletRequest httpRequest
    ) {
        log.info("Received request to save credit card in JSON format");
        CreditCard savedCard = saveCreditCardUseCase.execute(
            request.cardHolder(),
            request.cardNumber(),
            request.expirationDate(),
            httpRequest
        );
        log.info("Credit card saved successfully with ID: {}", savedCard.getId());

        CreditCardResponse response = CreditCardMapper.toResponse(savedCard);
        URI location = URI.create("/credit-cards/" + savedCard.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(
        summary = "Save credit card (Text)",
        description = "Saves a new credit card with encrypted data using text format (cardHolder,cardNumber,expirationDate)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Credit card created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreditCardResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request format or data",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<CreditCardResponse> saveCardText(
        @Parameter(description = "Plain text credit card data (format: cardHolder,cardNumber,expirationDate)", required = true)
        @RequestBody String textRequest,
        @Parameter(hidden = true)
        @AuthenticationPrincipal User user,
        @Parameter(hidden = true)
        HttpServletRequest httpRequest
    ) {
        log.info("Received request to save credit card in text format");
        String[] parts = textRequest.split(",");
        if (parts.length != 3) {
            throw new IllegalCardRequestFormatException("Invalid text format. Expected: cardHolder,cardNumber,expirationDate");
        }

        String cardHolder = parts[0].trim();
        String cardNumber = parts[1].trim();
        String expirationDate = parts[2].trim();

        CreditCard savedCard = saveCreditCardUseCase.execute(cardHolder, cardNumber, expirationDate, httpRequest);
        log.info("Credit card saved successfully with ID: {}", savedCard.getId());

        CreditCardResponse response = CreditCardMapper.toResponse(savedCard);
        URI location = URI.create("/credit-cards/" + savedCard.getId());
        return ResponseEntity.created(location).body(response);
    }
}
