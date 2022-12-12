package com.hoangvn.home24h.controllers.auth;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.configurations.sercurity.JwtUtil;
import com.hoangvn.home24h.models.token.Token;
import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.token.ITokenRepository;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;
import com.hoangvn.home24h.services.UserCreateUpdateService;

@RestController
@CrossOrigin
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private ITokenRepository tokenRepository;

    @Autowired
    UserCreateUpdateService createUpdateUserService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping(path = "/register")
    public ResponseEntity<Object> createUser(@RequestBody Map<String, Object> newuser) {
        Role userRole = roleRepository.findByRoleKey("ROLE_USER");
        try {
            User newUser = createUpdateUserService.createFromMap(newuser);
            newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
            newUser.setRole(userRole);
            return new ResponseEntity<>(userRepository.save(newUser), HttpStatus.CREATED);

        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<Object> getAllUsers(@RequestParam(required = false) String l) {
        try {
            List<User> users = userRepository.findAll();
            if ("all".equals(l)) {
                return new ResponseEntity<>(users.get(0).getCacBaiDang(), HttpStatus.OK);
            }
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

    @GetMapping("/role")
    public ResponseEntity<Object> getRole(@RequestBody String tk) {
        try {
            Token token = tokenRepository.findByTokenString(tk);
            System.out.println(token.toString());
            return new ResponseEntity<>(roleRepository.findByRoleKey("ROLE_ADMIN"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE) // More secure
    public ResponseEntity<Object> createNewUserForm(@RequestParam Map<String, Object> userFormMap) {

        return new ResponseEntity<>(userFormMap, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/user") // Not secure
    public ResponseEntity<Object> createNewUserBody(@RequestBody Map<String, Object> userJsonMap) {

        return new ResponseEntity<>(userJsonMap, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/user/{username}/posts")
    public ResponseEntity<Object> getAllPostsByUserName(@PathVariable String username) {
        try {
            Optional<User> optional = userRepository.findByUsername(username);
            if (optional.isPresent()) {
                return new ResponseEntity<>(optional.get().getCacBaiDang(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping(path = "/who")
    public ResponseEntity<Object> getUsernameByToken(@RequestBody(required = true) String token) {

        try {
            UserPrincipal user = jwtUtil.getUserFromToken(token);
            if (null != user.getUsername()) {
                return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
