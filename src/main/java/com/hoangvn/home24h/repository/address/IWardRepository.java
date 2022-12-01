package com.hoangvn.home24h.repository.address;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.address.Ward;

public interface IWardRepository extends JpaRepository<Ward, Long> {

}
