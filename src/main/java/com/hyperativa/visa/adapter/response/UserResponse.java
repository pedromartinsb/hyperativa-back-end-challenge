package com.hyperativa.visa.adapter.response;

public record UserResponse(
        String id,
        String username,
        String email,
        boolean enabled,
        String roles
) {}
