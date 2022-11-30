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
        List<User> users = userRepository.findAll();

        List<Permission> permissions = permissionRepository.findAll();

        List<Role> roles = roleRepository.findAll();

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

        if (roles.isEmpty()) {
            Permission create = permissionRepository.findByPermissionKey("CREATE");
            Permission read = permissionRepository.findByPermissionKey("READ");
            Permission update = permissionRepository.findByPermissionKey("UPDATE");
            Permission delete = permissionRepository.findByPermissionKey("DELETE");

            Set<Permission> permis = new HashSet<>();
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
        if (users.isEmpty()) {
            Set<Role> adRoles = new HashSet<>();
            adRoles.add(roleRepository.findByRoleName("ROLE_ADMIN"));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            admin.setHoTen("Admin");
            admin.setSoDienThoai("0123123123");
            admin.setRoles(adRoles);
            userRepository.save(admin);
            adRoles.clear();
        }

    }

}
