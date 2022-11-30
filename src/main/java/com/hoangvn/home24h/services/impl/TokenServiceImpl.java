package com.hoangvn.home24h.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoangvn.home24h.models.token.Token;
import com.hoangvn.home24h.repository.token.ITokenRepository;
import com.hoangvn.home24h.services.ITokenService;

@Service
public class TokenServiceImpl implements ITokenService {
    @Autowired
    ITokenRepository tokenRepository;

    @Override
    public Token createToken(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token findByToken(String tokenString) {
        return tokenRepository.findByTokenString(tokenString);
    }

}
