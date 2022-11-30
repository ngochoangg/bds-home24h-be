package com.hoangvn.home24h.repository.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.address.Province;

public interface IProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByProvinceCode(String provinceCode);
}
