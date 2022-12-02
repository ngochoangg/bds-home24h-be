package com.hoangvn.home24h.controllers;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.address.Ward;
import com.hoangvn.home24h.repository.address.IWardRepository;

@RestController
@CrossOrigin
public class WardController {
    @Autowired
    IWardRepository wardRepository;

    @GetMapping(path = "/ward")
    public ResponseEntity<Object> getAll(@RequestParam(required = false, defaultValue = "0") String p) {
        try {
            Pageable pageOfHundred = PageRequest.of(Integer.parseInt(p), 50);
            Set<? extends Ward> wards = wardRepository.findAll(pageOfHundred).toSet();
            return new ResponseEntity<>(wards, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping(path = "/ward/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            Optional<? extends Ward> optional = wardRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
