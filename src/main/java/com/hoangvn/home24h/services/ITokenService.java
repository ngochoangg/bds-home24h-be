package com.hoangvn.home24h.services;

import com.hoangvn.home24h.models.token.Token;

public interface ITokenService {
    Token createToken(Token token);

    Token findByToken(String tokenString);
}
