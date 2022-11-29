package com.hoangvn.home24h.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoangvn.home24h.models.address.Address;

public interface IAddressRepository extends JpaRepository<Address, Long> {

}
