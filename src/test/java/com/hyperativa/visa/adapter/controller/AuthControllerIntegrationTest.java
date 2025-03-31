package com.hyperativa.visa.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperativa.visa.TestVisaApplication;
import com.hyperativa.visa.adapter.request.LoginRequest;
import com.hyperativa.visa.adapter.response.LoginResponse;
import com.hyperativa.visa.config.security.JwtUtils;
import com.hyperativa.visa.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestVisaApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";
    private static final String TOKEN = "valid.jwt.token";

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        User user = new User();
        user.setUsername(USERNAME);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtUtils.createToken(any(User.class))).thenReturn(TOKEN);

        // When & Then
        MvcResult result = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(TOKEN))
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response body: " + responseBody);

        LoginResponse response = objectMapper.readValue(responseBody, LoginResponse.class);
        assertEquals(TOKEN, response.accessToken());
        assertEquals("Bearer", response.tokenType());
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsMissing() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("", PASSWORD);

        // When & Then
        MvcResult result = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("'username': O nome de usuário é obrigatório."))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest(USERNAME, "");

        // When & Then
        MvcResult result = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("'password': A senha é obrigatória."))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        MvcResult result = mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Usuário e/ou Senha estão incorretos."))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }
} 