package com.hyperativa.visa.infrastructure.repository;

import com.hyperativa.visa.domain.model.User;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import com.hyperativa.visa.infrastructure.mapper.UserEntityMapper;
import com.hyperativa.visa.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.hyperativa.visa.infrastructure.mapper.UserEntityMapper.toDomain;
import static com.hyperativa.visa.infrastructure.mapper.UserEntityMapper.toEntity;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(final User user) {
        final var entity = toEntity(user);
        final var savedEntity = userJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(final String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserEntityMapper::toDomain);
    }
}
