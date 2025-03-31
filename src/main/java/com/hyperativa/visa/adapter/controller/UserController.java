package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.CreateUserRequest;
import com.hyperativa.visa.adapter.response.ErrorResponse;
import com.hyperativa.visa.adapter.response.UserResponse;
import com.hyperativa.visa.application.usecase.CreateUserUseCase;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API for managing user operations")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @Operation(
        summary = "Create new user",
        description = "Creates a new user with the provided information"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class)
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(
        @Parameter(description = "User data", required = true)
        @Valid @RequestBody CreateUserRequest request,
        UriComponentsBuilder uriBuilder
    ) {
        log.info("Received request to create user with username: {}", request.username());
        try {
            final var user = createUserUseCase.execute(
                request.username(),
                request.email(),
                request.password(),
                request.roles()
            );

            final var response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.getRoles()
            );

            URI location = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(response.id())
                .toUri();
            
            log.info("User created successfully with ID: {}", response.id());
            return ResponseEntity.created(location).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
