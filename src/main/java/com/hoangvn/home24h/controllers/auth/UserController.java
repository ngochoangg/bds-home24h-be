package com.hoangvn.home24h.controllers.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.configurations.sercurity.JwtUtil;
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

    // CREATE
    @PostMapping(path = "/register")
    public ResponseEntity<Object> createUser(@RequestBody Map<String, Object> newuser) {
        Set<Role> roleUser = new HashSet<>();
        roleUser.add(roleRepository.findByRoleKey("ROLE_USER"));
        try {
            User newUser = createUpdateUserService.createFromMap(newuser);
            newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
            newUser.setRole(roleUser);
            return new ResponseEntity<>(userRepository.save(newUser), HttpStatus.CREATED);

        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Kiểm tra 2 bước, khi trả về token cho endpoint
    // lưu token tại client, khi truy cập vào admin thì
    // kiểm tra xem token có trả về đúng người dùng không.
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

    // READ ( only ADMIN)

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getPassword(@RequestBody String token) {
        try {
            return new ResponseEntity<>(tokenRepository.findByTokenString(token), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER') or #username == authentication.name")
    public ResponseEntity<Object> getRole(@RequestParam(required = false) String username) {
        try {
            Optional<User> optional = userRepository.findByUsername(username);
            if (optional.isPresent()) {
                return new ResponseEntity<>(optional.get().getRole(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Lấy danh sách bài đăng của người dùng, người dùng
    // này không thể lấy của người kia ngoại trừ admin.
    @GetMapping(path = "/{username}/posts")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
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

    // UPDATE
    @PutMapping(path = "/user/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (#username == authentication.name and #user['role']==null)")
    public ResponseEntity<Object> updateUser(@RequestBody Map<String, Object> user,
            @PathVariable String username) {
        try {
            User updatedUser = createUpdateUserService.updateUserFromMap(user, username);
            if (null == updatedUser) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(userRepository.save(updatedUser), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping(path = "/user/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        try {
            Optional<User> optional = userRepository.findByUsername(username);
            if (optional.isPresent()) {
                userRepository.delete(optional.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
