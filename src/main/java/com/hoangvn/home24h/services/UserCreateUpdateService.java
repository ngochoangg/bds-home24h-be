package com.hoangvn.home24h.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;

@Service
public class UserCreateUpdateService {

    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    IUserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    /**
     * @param user <== Map < String, Object >
     * 
     *             <pre>Cấu trúc user
     *             {
     *             <b>hoTen</b>:...,
     *             <b>soDienThoai</b>:...,
     *             <b>email</b>:...,
     *             <b>diaChi</b>:...,
     *             <b>username</b>:...,
     *             <b>password</b>:...,
     *             <b>cacBaiDang</b>:...
     *             } 
     *             - role : Mặc định role của người dùng mới là {@code ROLE_USER} chỉ được cập nhật và tạo mới
     *             </p>
     *             - username, email là duy nhất( unique)
     *             - cacBaiDang: Danh sách các bài đăng chỉ trả về khi được gọi, mặc định sẽ không gọi để db không quá tải
     *             </pre>
     * 
     * 
     * 
     * @return user là một người dùng theo class {@link User} đã được tạo mới
     *         hoặc cập nhật
     */

    public User createFromMap(Map<String, Object> user) {
        User newUser = modelMapper.map(user, User.class);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        return newUser;
    }

    public User updateUserFromMap(Map<String, Object> user, String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty()) {
            return null;
        }
        User updatedUser = optional.get();
        modelMapper.map(user, updatedUser);
        if (user.containsKey("role")) {
            Set<Role> roles = extractRoleFromMap(user);
            updatedUser.setRole(roles);
        }
        updatedUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));

        return updatedUser;
    }

    // Role check and return
    private Set<Role> extractRoleFromMap(Map<String, Object> user) {
        Set<Role> roles = new HashSet<>();
        Object rolesVal = user.get("role");
        if (rolesVal instanceof String) {
            Role role = roleRepository.findByRoleKey((String) rolesVal);
            roles.add(role);
        } else if (rolesVal instanceof Collection) {
            Collection<String> roleNames = (Collection<String>) rolesVal;
            roleNames.forEach(r -> {
                Role role = roleRepository.findByRoleKey(r);
                roles.add(role);
            });
        }
        return roles;
    }
}
