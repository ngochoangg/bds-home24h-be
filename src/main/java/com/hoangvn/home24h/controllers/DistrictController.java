package com.hoangvn.home24h.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.address.District;
import com.hoangvn.home24h.models.address.Ward;
import com.hoangvn.home24h.repository.address.IDistrictRepository;

@RestController
@CrossOrigin
public class DistrictController {
    @Autowired
    IDistrictRepository districtRepository;

    @GetMapping(path = "/district")
    public ResponseEntity<Object> getAll(@RequestParam(required = false, defaultValue = "0") String p) {

        try {
            Pageable pageOfTen = PageRequest.of(Integer.parseInt(p), 10);
            List<District> districts = new ArrayList<>();
            districtRepository.findAll(pageOfTen).forEach(districts::add);
            return new ResponseEntity<>(districts, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }

    }

    @GetMapping(path = "/districts/{prefix}")
    public ResponseEntity<Object> getByPrefix(@PathVariable String prefix) {
        try {
            return new ResponseEntity<>(districtRepository.findByPrefix(prefix), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping(path = "/district/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            Optional<District> optional = districtRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping(path = "/district/{id}/w")
    public ResponseEntity<Object> getWardByDistrictId(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") String w) {
        try {
            Optional<District> optional = districtRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Set<Ward> wards = optional.get().getWards();

            if (Long.parseLong(w) > 0) {
                List<Ward> res = new ArrayList<>();
                wards.forEach(d -> {
                    if (Long.parseLong(w) == d.getId()) {
                        res.add(d);
                    }
                });
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            return new ResponseEntity<>(wards, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
