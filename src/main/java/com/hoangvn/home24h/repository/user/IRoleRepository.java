package com.hoangvn.home24h.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.user.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleKey(String key);
}
