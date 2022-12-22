package com.hoangvn.home24h.models.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @NotBlank
    private String roleName;
    @NotBlank
    private String roleKey;

    @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "permission_id") })
    private Set<Permission> permission = new HashSet<>();

    public Role() {
    }

    public Role(@NotBlank String roleName, @NotBlank String roleKey, Set<Permission> permission) {
        this.roleName = roleName;
        this.roleKey = roleKey;
        this.permission = permission;
    }

    public Set<Permission> getPermission() {
        return permission;
    }

    public void setPermission(Set<Permission> permission) {
        this.permission = permission;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }
}
