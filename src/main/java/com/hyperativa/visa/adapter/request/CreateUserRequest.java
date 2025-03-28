package com.hyperativa.visa.adapter.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        @Size(max = 100)
        String username,
        @NotBlank(message = "O email é obrigatório.")
        @Email
        @Size(max = 255)
        String email,
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, max = 255)
        String password,
        @NotBlank(message = "Os papéis são obrigatórios.")
        String roles
) {}
