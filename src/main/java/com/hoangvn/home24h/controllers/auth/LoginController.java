package com.hoangvn.home24h.controllers.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.configurations.sercurity.JwtUtil;
import com.hoangvn.home24h.models.token.Token;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.services.ITokenService;
import com.hoangvn.home24h.services.IUserService;

@RestController
@CrossOrigin
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private ITokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestBody User user) {
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        if (null == user || !new BCryptPasswordEncoder().matches(user.getPassword(),
                userPrincipal.getPassword())) {
            LOGGER.error("Error: ");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password not match! ");
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setExpireDate(jwtUtil.generateExpireDate());
        token.setCreatedBy(userPrincipal.getUserId());
        return new ResponseEntity<>(token.getToken(), HttpStatus.OK);
    }
}
