package com.hoangvn.home24h.repository.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoangvn.home24h.models.address.District;

public interface IDistrictRepository extends JpaRepository<District, Long> {
    District findByName(String districtName);

    @Query(value = "SELECT * FROM district WHERE prefix LIKE :prefix%", nativeQuery = true)
    List<District> findByPrefix(@Param("prefix") String prefix);
}
