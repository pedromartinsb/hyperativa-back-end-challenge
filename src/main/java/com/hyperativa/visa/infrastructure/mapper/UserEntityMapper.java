package com.hyperativa.visa.infrastructure.mapper;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.infrastructure.repository.jpa.UserEntity;

public class UserEntityMapper {

    public static User toDomain(final UserEntity entity) {
        if (entity == null) return null;
        final var domain = new User(
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.isEnabled(),
                entity.getRoles()
        );
        domain.setId(entity.getId());
        return domain;
    }

    public static UserEntity toEntity(final User domain) {
        final var entity = new UserEntity();
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setEnabled(domain.isEnabled());
        entity.setRoles(domain.getRoles());
        return entity;
    }
}
