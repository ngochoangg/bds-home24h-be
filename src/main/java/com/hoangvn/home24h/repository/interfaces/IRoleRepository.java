package com.hoangvn.home24h.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.user.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
