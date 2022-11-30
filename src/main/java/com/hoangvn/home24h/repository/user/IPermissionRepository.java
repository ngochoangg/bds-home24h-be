package com.hoangvn.home24h.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.user.Permission;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    public Permission findByPermissionKey(String permissionKey);
}
