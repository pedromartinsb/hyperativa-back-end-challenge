package com.hyperativa.visa.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperativa.visa.TestVisaApplication;
import com.hyperativa.visa.adapter.mapper.CreditCardMapper;
import com.hyperativa.visa.adapter.request.CreditCardRequest;
import com.hyperativa.visa.adapter.response.CreditCardResponse;
import com.hyperativa.visa.adapter.response.IdentifierResponse;
import com.hyperativa.visa.application.usecase.FindCardIdentifierUseCase;
import com.hyperativa.visa.application.usecase.SaveCreditCardUseCase;
import com.hyperativa.visa.common.response.ErrorResponse;
import com.hyperativa.visa.domain.model.CreditCard;
import com.hyperativa.visa.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestVisaApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CreditCardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaveCreditCardUseCase saveCreditCardUseCase;

    @MockBean
    private FindCardIdentifierUseCase findCardIdentifierUseCase;

    @MockBean
    private HttpServletRequest request;

    private static final String CARD_NUMBER = "4532123456789012";
    private static final String CARD_HOLDER = "John Doe";
    private static final String EXPIRATION_DATE = "12/25";
    private static final String CARD_ID = "card-id";
    private static final String CARD_IDENTIFIER = "card-identifier";
    private static final String ENCRYPTED_CARD_NUMBER = "encrypted-card-number";
    private static final String CARD_NUMBER_HASH = "card-number-hash";

    @Test
    void shouldGetCardIdentifierSuccessfully() throws Exception {
        // Given
        when(findCardIdentifierUseCase.execute(CARD_NUMBER))
            .thenReturn(Optional.of(CARD_IDENTIFIER));

        // When & Then
        MvcResult result = mockMvc.perform(get("/credit-cards/identifier")
                .param("cardNumber", CARD_NUMBER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identifier").value(CARD_IDENTIFIER))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);

        IdentifierResponse response = objectMapper.readValue(responseBody, IdentifierResponse.class);
        assertEquals(CARD_IDENTIFIER, response.identifier());
    }

    @Test
    void shouldReturnNotFoundWhenCardIdentifierDoesNotExist() throws Exception {
        // Given
        when(findCardIdentifierUseCase.execute(CARD_NUMBER))
            .thenReturn(Optional.empty());

        String maskedCardNumber = CreditCardMapper.maskCardNumber(CARD_NUMBER);
        String expectedMessage = "Cartão não encontrado para o número fornecido: " + maskedCardNumber;

        // When & Then
        MvcResult result = mockMvc.perform(get("/credit-cards/identifier")
                .param("cardNumber", CARD_NUMBER))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value(expectedMessage))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Error response: " + responseBody);

        ErrorResponse response = objectMapper.readValue(responseBody, ErrorResponse.class);
        assertEquals("Not Found", response.getError());
        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    void shouldSaveCardSuccessfully() throws Exception {
        // Given
        CreditCardRequest request = new CreditCardRequest(
            CARD_HOLDER,
            CARD_NUMBER,
            EXPIRATION_DATE
        );

        User user = new User();
        user.setUsername("testuser");

        CreditCard mockCard = new CreditCard(
            CARD_HOLDER,
            ENCRYPTED_CARD_NUMBER,
            EXPIRATION_DATE,
            CARD_NUMBER_HASH,
            user
        );
        mockCard.setId(CARD_ID);

        when(saveCreditCardUseCase.execute(any(), any(), any(), any())).thenReturn(mockCard);

        // When & Then
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").value(CARD_ID))
            .andExpect(jsonPath("$.cardHolder").value(CARD_HOLDER))
            .andExpect(jsonPath("$.expirationDate").value(EXPIRATION_DATE))
            .andExpect(jsonPath("$.maskedCardNumber").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response body: " + responseBody);
        System.out.println("Location header: " + result.getResponse().getHeader("Location"));

        CreditCardResponse response = objectMapper.readValue(responseBody, CreditCardResponse.class);
        assertEquals(CARD_ID, response.id());
        assertEquals(CARD_HOLDER, response.cardHolder());
        assertEquals(EXPIRATION_DATE, response.expirationDate());
        assertNotNull(response.maskedCardNumber());
    }

    @Test
    void shouldSaveCardFromTextSuccessfully() throws Exception {
        // Given
        String textRequest = CARD_HOLDER + "," + CARD_NUMBER + "," + EXPIRATION_DATE;

        User user = new User();
        user.setUsername("testuser");

        CreditCard mockCard = new CreditCard(
            CARD_HOLDER,
            ENCRYPTED_CARD_NUMBER,
            EXPIRATION_DATE,
            CARD_NUMBER_HASH,
            user
        );
        mockCard.setId(CARD_ID);

        when(saveCreditCardUseCase.execute(any(), any(), any(), any())).thenReturn(mockCard);

        // When & Then
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .contentType(MediaType.TEXT_PLAIN)
                .content(textRequest))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").value(CARD_ID))
            .andExpect(jsonPath("$.cardHolder").value(CARD_HOLDER))
            .andExpect(jsonPath("$.expirationDate").value(EXPIRATION_DATE))
            .andExpect(jsonPath("$.maskedCardNumber").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response body: " + responseBody);
        System.out.println("Location header: " + result.getResponse().getHeader("Location"));

        CreditCardResponse response = objectMapper.readValue(responseBody, CreditCardResponse.class);
        assertEquals(CARD_ID, response.id());
        assertEquals(CARD_HOLDER, response.cardHolder());
        assertEquals(EXPIRATION_DATE, response.expirationDate());
        assertNotNull(response.maskedCardNumber());
    }

    @Test
    void shouldReturnBadRequestWhenCardNumberIsInvalid() throws Exception {
        // Given
        CreditCardRequest request = new CreditCardRequest(
            CARD_HOLDER,
            "invalid-card",
            EXPIRATION_DATE
        );

        // When & Then
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }

    @Test
    void shouldReturnBadRequestWhenExpirationDateIsInvalid() throws Exception {
        // Given
        CreditCardRequest request = new CreditCardRequest(
            CARD_HOLDER,
            CARD_NUMBER,
            "invalid-date"
        );

        // When & Then
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }
} 