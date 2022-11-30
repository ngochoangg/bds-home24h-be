package com.hoangvn.home24h.repository.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.address.District;

@Repository
public interface IDistrictRepository extends JpaRepository<District, Long> {

}
