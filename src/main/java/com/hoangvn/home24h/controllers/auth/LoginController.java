package com.hoangvn.home24h.controllers.auth;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.configurations.sercurity.JwtUtil;
import com.hoangvn.home24h.models.token.Token;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.services.ITokenService;
import com.hoangvn.home24h.services.IUserService;

@RestController
@CrossOrigin
public class LoginController {
    Logger logger = Logger.getLogger(LoginController.class.getName());
    @Autowired
    private IUserService userService;
    @Autowired
    private ITokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> loginWithForm(@RequestParam Map<String, Object> formHeader) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.convertValue(formHeader, User.class);
            UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
            if (this.comparePassword(user.getPassword(), userPrincipal.getPassword())) {
                return new ResponseEntity<>(this.generateToken(userPrincipal), HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password not match! ");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loginWithJson(@RequestBody Map<String, Object> jsonBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.convertValue(jsonBody, User.class);
            UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
            if (this.comparePassword(user.getPassword(), userPrincipal.getPassword())) {
                return new ResponseEntity<>(this.generateToken(userPrincipal), HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password not match! ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * @param rawPassword
     * @param encodedPassword
     * @return true / false
     */
    private boolean comparePassword(String rawPassword, String encodedPassword) {
        final boolean result = new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
        return result;
    }

    private String generateToken(UserPrincipal userPrincipal) {
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setExpireDate(jwtUtil.generateExpireDate());
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(token);
        return token.getToken();
    }
}
