package com.hoangvn.home24h.repository.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.address.Province;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByProvinceCode(String provinceCode);
}
