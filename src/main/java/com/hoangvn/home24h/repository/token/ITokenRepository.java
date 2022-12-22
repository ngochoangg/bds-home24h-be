package com.hoangvn.home24h.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.token.Token;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    Token findByTokenString(String token);
}
