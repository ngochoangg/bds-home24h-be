package com.hoangvn.home24h.repository.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.address.Province;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByProvinceCode(String provinceCode);

    @Query(value = "SELECT * FROM province WHERE name =:provinceName", nativeQuery = true)
    Province findByProvinceName(@Param("provinceName") String provinceName);
}
