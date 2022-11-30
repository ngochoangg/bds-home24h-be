package com.hoangvn.home24h.services.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.user.IUserRepository;
import com.hoangvn.home24h.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public UserPrincipal findByUsername(String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal();
        if (optional.isPresent()) {
            Set<String> authorities = new HashSet<>();
            User thisUser = optional.get();
            thisUser.getRoles().forEach(r -> {
                authorities.add(r.getRoleKey());
                r.getPermission().forEach(p -> authorities.add(p.getPermissionKey()));
            });

            userPrincipal.setUserId(thisUser.getId());
            userPrincipal.setUserName(thisUser.getUsername());
            userPrincipal.setPassword(thisUser.getPassword());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }

}
