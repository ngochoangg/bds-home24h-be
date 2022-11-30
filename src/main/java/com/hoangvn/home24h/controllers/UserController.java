package com.hoangvn.home24h.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.token.ITokenRepository;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;
import com.hoangvn.home24h.services.IUserService;

@RestController
@CrossOrigin
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private ITokenRepository tokenRepository;

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User newuser) {
        Role userRole = roleRepository.findByRoleKey("ROLE_USER");
        try {
            if (null != newuser) {

                newuser.setPassword(new BCryptPasswordEncoder().encode(newuser.getPassword()));
                newuser.setRole(userRole);
                User created = userService.createUser(newuser);
                return new ResponseEntity<>(created, HttpStatus.CREATED);
            }
            return ResponseEntity.unprocessableEntity().body("Failed to create");

        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getCause().getCause().getMessage());
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getCause().getCause().getMessage());
        }
    }

    @GetMapping("/pass")
    @PreAuthorize("hasAnyAuthority('CREATE','DELETE')")
    public ResponseEntity<Object> getPassword(@RequestBody String token) {
        try {
            return new ResponseEntity<>(tokenRepository.findByTokenString(token), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
