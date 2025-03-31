package com.hyperativa.visa.application.usecase;

import com.hyperativa.visa.config.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class GetUsernameByRequestUseCase {

    private final JwtUtils jwtUtils;

    public GetUsernameByRequestUseCase(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String execute(final HttpServletRequest request) {
        final var bearerToken = jwtUtils.resolveToken(request);
        return jwtUtils.getUserNameFromJwtToken(bearerToken);
    }
}
