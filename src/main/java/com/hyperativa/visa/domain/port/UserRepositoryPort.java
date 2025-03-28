package com.hyperativa.visa.domain.port;

import com.hyperativa.visa.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUsername(String username);
}
