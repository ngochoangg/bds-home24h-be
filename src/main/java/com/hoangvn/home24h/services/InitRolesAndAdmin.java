package com.hoangvn.home24h.services;

import com.hoangvn.home24h.models.user.Permission;
import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.models.user.User;

import com.hoangvn.home24h.repository.user.IPermissionRepository;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

@Component
public class InitRolesAndAdmin {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IPermissionRepository permissionRepository;

    @PostConstruct
    public void initUser() {

        // Initialize permissions
        if (permissionRepository.findAll().isEmpty()) {
            permissionRepository.saveAll(Arrays.asList(
                    new Permission("READ", "Read access"),
                    new Permission("WRITE", "Write access"),
                    new Permission("DELETE", "Delete access"),
                    new Permission("UPDATE", "Update access")));
        }

        // Initialize roles
        if (roleRepository.findAll().isEmpty()) {
            Set<Permission> adminPermissions = new HashSet<>(permissionRepository.findAll());
            roleRepository.saveAll(Arrays.asList(
                    new Role("ROLE_ADMIN", "Administrator", adminPermissions),
                    new Role("ROLE_USER", "User", new HashSet<>(Arrays.asList(
                            permissionRepository.findByPermissionKey("READ"),
                            permissionRepository.findByPermissionKey("WRITE"))))));
        }

        // Initialize user
        if (userRepository.findAll().isEmpty()) {
            Role adminRole = roleRepository.findByRoleKey("ROLE_ADMIN");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            admin.setHoTen("Admin");
            admin.setSoDienThoai("0123123123");
            admin.setRole(new HashSet<>(Arrays.asList(adminRole)));
            userRepository.save(admin);
        }

    }

}
