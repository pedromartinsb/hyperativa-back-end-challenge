package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.LoginRequest;
import com.hyperativa.visa.adapter.response.LoginResponse;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.config.security.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Tentando autenticar o usuário: {}", loginRequest.username());
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            final var username = authentication.getName();
            final var user = new User();
            user.setUsername(username);
            final var token = jwtUtils.createToken(user);

            logger.info("Autenticação realizada com sucesso para: {}", loginRequest.username());
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception e) {
            logger.error("Erro ao autenticar o usuário: {}", loginRequest.username(), e);
            throw e;
        }
    }
}
