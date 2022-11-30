package com.hoangvn.home24h.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.user.Permission;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    public Permission findByPermissionKey(String permissionKey);
}
