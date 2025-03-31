package com.hyperativa.visa.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperativa.visa.TestVisaApplication;
import com.hyperativa.visa.adapter.request.CreateUserRequest;
import com.hyperativa.visa.adapter.response.UserResponse;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepositoryPort userRepositoryPort;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "testuser",
            "test@example.com",
            "password123",
            "ROLE_USER"
        );

        User mockUser = new User("testuser", "test@example.com", "encoded_password", true, "ROLE_USER");
        mockUser.setId("user-id");

        when(userRepositoryPort.findByUsername(request.username())).thenReturn(java.util.Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenReturn(mockUser);

        // When & Then
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.enabled").value(true))
            .andExpect(jsonPath("$.roles").value("ROLE_USER"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response body: " + responseBody);
        System.out.println("Location header: " + result.getResponse().getHeader("Location"));

        UserResponse response = objectMapper.readValue(responseBody, UserResponse.class);

        assertNotNull(response.id());
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());
        assertTrue(response.enabled());
        assertEquals("ROLE_USER", response.roles());
    }

    @Test
    void shouldReturnBadRequestWhenUsernameExists() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "existinguser",
            "test@example.com",
            "password123",
            "ROLE_USER"
        );

        when(userRepositoryPort.findByUsername("existinguser")).thenReturn(java.util.Optional.of(new User()));

        // When & Then
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Username already exists"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Error response: " + responseBody);
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "testuser",
            "invalid-email",
            "password123",
            "ROLE_USER"
        );

        // When & Then
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Email validation response: " + responseBody);
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "testuser",
            "test@example.com",
            "short",
            "ROLE_USER"
        );

        // When & Then
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Password validation response: " + responseBody);
    }
} 