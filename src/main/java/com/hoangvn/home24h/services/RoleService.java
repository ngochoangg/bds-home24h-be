package com.hoangvn.home24h.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoangvn.home24h.models.user.Permission;
import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.repository.user.IPermissionRepository;
import com.hoangvn.home24h.repository.user.IRoleRepository;

@Service
public class RoleService {
    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IPermissionRepository permissionRepository;

    ModelMapper modelMapper = new ModelMapper();

    public Role createRoleFromMap(Map<String, Object> roleMap) {
        Role role = new Role();

        role.setRoleKey((String) roleMap.get("roleKey"));
        role.setRoleName((String) roleMap.get("roleName"));

        Object permissionVal = roleMap.get("permission");
        if (null != permissionVal) {
            Set<Permission> permissions = extractPermissionMap(permissionVal);
            role.setPermission(permissions);
        }
        return role;
    }

    public Role updateFromMap(Map<String, Object> roleMap, String roleKey) {

        Role updatedRole = roleRepository.findByRoleKey(roleKey);

        if (null == updatedRole) {
            return null;
        }
        modelMapper.map(roleMap, updatedRole);
        if (roleMap.containsKey("permission")) {
            Set<Permission> permissions = extractPermissionMap(roleMap);
            updatedRole.setPermission(permissions);
        }

        return updatedRole;

    }

    private Set<Permission> extractPermissionMap(Object permissionObject) {
        Set<Permission> permissions = new HashSet<>();
        if (permissionObject instanceof String) {
            Permission permission = permissionRepository.findByPermissionKey((String) permissionObject);
            permissions.add(permission);
        } else if (permissionObject instanceof Collection) {
            Collection<String> permissionTypes = (Collection<String>) permissionObject;
            permissionTypes.forEach(p -> permissions.add(permissionRepository.findByPermissionKey(p)));
        }
        return permissions;
    }
}
