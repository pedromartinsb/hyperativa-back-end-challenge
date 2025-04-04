package com.hyperativa.visa.adapter.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        String username,
        @NotBlank(message = "A senha é obrigatória.")
        String password
) {}
