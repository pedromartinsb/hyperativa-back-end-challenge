package com.hyperativa.visa.adapter.controller;

import com.hyperativa.visa.adapter.request.CreateUserRequest;
import com.hyperativa.visa.adapter.response.UserResponse;
import com.hyperativa.visa.application.usecase.CreateUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request, UriComponentsBuilder uriBuilder) {
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
                .path("/api/v1/users/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
