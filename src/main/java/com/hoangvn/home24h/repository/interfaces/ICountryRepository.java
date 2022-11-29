package com.hoangvn.home24h.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.address.Country;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Long> {
    public Optional<Country> findByCountryCode(String code);
}
