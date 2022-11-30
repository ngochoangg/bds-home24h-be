package com.hoangvn.home24h.services;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.models.user.User;

public interface IUserService {
    User createUser(User user);

    UserPrincipal findByUsername(String username);
}
