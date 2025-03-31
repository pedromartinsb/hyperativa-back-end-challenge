package com.hyperativa.visa.config.security;

import com.hyperativa.visa.infrastructure.repository.jpa.UserEntity;
import com.hyperativa.visa.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserEntity user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }
}
