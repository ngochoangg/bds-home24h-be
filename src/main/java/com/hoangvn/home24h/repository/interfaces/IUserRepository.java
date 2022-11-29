package com.hoangvn.home24h.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.user.User;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
