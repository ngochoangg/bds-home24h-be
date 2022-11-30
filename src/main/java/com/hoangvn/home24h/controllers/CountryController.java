package com.hoangvn.home24h.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import com.hoangvn.home24h.repository.address.IAddressRepository;
import com.hoangvn.home24h.repository.address.ICountryRepository;

@RestController
@CrossOrigin
public class CountryController {
    @Autowired
    ICountryRepository countryRepository;
    @Autowired
    IAddressRepository addressRepository;

    @GetMapping(value = "/countries")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/countries/{code}")
    public ResponseEntity<Object> getByCode(@PathVariable String code) {
        var optional = countryRepository.findByCountryCode(code);
        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/countries/{code}/r")
    public ResponseEntity<Object> getAllRegionByCountryCode(@PathVariable String code) {
        var optional = countryRepository.findByCountryCode(code);
        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get().getRegions(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/address")
    public ResponseEntity<Object> getAllAddress() {
        return new ResponseEntity<>(addressRepository.findAll(), HttpStatus.OK);
    }

}
