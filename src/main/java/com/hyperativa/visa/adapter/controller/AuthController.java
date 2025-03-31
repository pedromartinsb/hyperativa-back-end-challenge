package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.LoginRequest;
import com.hyperativa.visa.adapter.response.LoginResponse;
import com.hyperativa.visa.adapter.response.ErrorResponse;
import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.config.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for managing authentication operations")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LoginResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials or request format",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid credentials",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> authenticateUser(
        @Parameter(description = "Login credentials", required = true)
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        log.info("Attempting to authenticate user: {}", loginRequest.username());
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        
        final var username = authentication.getName();
        final var user = new User();
        user.setUsername(username);
        final var token = jwtUtils.createToken(user);

        log.info("Authentication successful for user: {}", loginRequest.username());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
