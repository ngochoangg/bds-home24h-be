package com.hoangvn.home24h.services;

import java.util.Set;

import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.models.user.BaseEntity;

public abstract class IUserLogin extends BaseEntity {
    String username;

    String password;

    Set<Role> roles;
}
