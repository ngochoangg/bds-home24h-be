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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

@Component
public class CInitRolesAndAdmin {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IPermissionRepository permissionRepository;

    @PostConstruct
    public void initUser() {

        List<Permission> permissions = permissionRepository.findAll();

        Set<Permission> permis = new HashSet<>();

        if (permissions.isEmpty()) {
            // Creeate
            Permission createPermission = new Permission();
            createPermission.setPermissionKey("CREATE");
            createPermission.setPermissionName("Create user");
            createPermission.setDeleted(false);

            // Read
            Permission readPermission = new Permission();
            readPermission.setPermissionKey("READ");
            readPermission.setPermissionName("Read user list");
            readPermission.setDeleted(false);

            // Update
            Permission updatePermission = new Permission();
            updatePermission.setPermissionKey("UPDATE");
            updatePermission.setPermissionName("Update user");
            updatePermission.setDeleted(false);

            // Delete
            Permission deletePermission = new Permission();
            deletePermission.setPermissionKey("DELETE");
            deletePermission.setPermissionName("Delete user");
            updatePermission.setDeleted(false);

            permissionRepository.save(createPermission);

            permissionRepository.save(readPermission);

            permissionRepository.save(updatePermission);

            permissionRepository.save(deletePermission);

        }

        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            permis.clear();
            Permission create = permissionRepository.findByPermissionKey("CREATE");
            Permission read = permissionRepository.findByPermissionKey("READ");
            Permission update = permissionRepository.findByPermissionKey("UPDATE");
            Permission delete = permissionRepository.findByPermissionKey("DELETE");
            permis.add(create);
            permis.add(read);
            permis.add(update);
            permis.add(delete);
            // Admin role
            Role adminRole = new Role();
            adminRole.setRoleKey("ROLE_ADMIN");
            adminRole.setRoleName("Administrator");
            adminRole.setPermission(permis);

            roleRepository.save(adminRole);
            permis.clear();

            permis.add(create);
            permis.add(update);
            // User role
            Role userRole = new Role();
            userRole.setRoleKey("ROLE_USER");
            userRole.setRoleName("User logged in");
            userRole.setPermission(permis);

            roleRepository.save(userRole);

        }
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            // Admin role
            Optional<Role> aOptional = roleRepository.findById(1l);
            if (aOptional.isPresent()) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
                admin.setHoTen("Admin");
                admin.setSoDienThoai("0123123123");
                admin.setRole(aOptional.get());
                userRepository.save(admin);
            }

        }

    }

}
