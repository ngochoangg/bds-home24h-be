package com.hoangvn.home24h.services;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;

@Service
public class UserCreateUpdateService {

    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    IUserRepository userRepository;

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

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(user, User.class);
    }

    public User updateUserFromMap(Map<String, Object> user, String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isPresent()) {
            User thisUser = optional.get();
            user.entrySet().forEach(kv -> {
                if ("username".equals(kv.getKey())) {
                    thisUser.setUsername(kv.getValue().toString());
                }
                if ("hoTen".equals(kv.getKey())) {
                    thisUser.setHoTen(kv.getValue().toString());
                }
                if ("soDienThoai".equals(kv.getKey())) {
                    thisUser.setSoDienThoai(kv.getValue().toString());
                }
                if ("email".equals(kv.getKey())) {
                    thisUser.setEmail(kv.getValue().toString());
                }
                if ("diaChi".equals(kv.getKey())) {
                    thisUser.setDiaChi(kv.getValue().toString());
                }
            });
            return thisUser;
        }

        return null;
    }
}
