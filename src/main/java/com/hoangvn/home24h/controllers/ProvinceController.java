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
import org.springframework.web.bind.annotation.GetMapping;

import com.hoangvn.home24h.models.address.District;
import com.hoangvn.home24h.models.address.Province;
import com.hoangvn.home24h.repository.address.IDistrictRepository;
import com.hoangvn.home24h.repository.address.IProvinceRepository;

@RestController
@CrossOrigin
public class ProvinceController {
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IDistrictRepository districtRepository;

    @GetMapping(value = "/province")
    public ResponseEntity<Object> getAll(@RequestParam(required = false, defaultValue = "0") String p,
            @RequestParam(required = false, defaultValue = "10") String s) {
        try {
            Pageable pageOfParamS = PageRequest.of(Integer.parseInt(p), Integer.parseInt(s));
            List<Province> list = new ArrayList<>();
            provinceRepository.findAll(pageOfParamS).forEach(list::add);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping("/province/{code}")
    public ResponseEntity<Object> getByCode(@PathVariable String code) {
        try {
            Optional<Province> optional = provinceRepository.findByProvinceCode(code);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping("/province/{code}/d")
    public ResponseEntity<Object> getAllDistrictByProvinceCode(@PathVariable String code,
            @RequestParam(required = false, defaultValue = "0") String id) {
        try {
            Optional<Province> optional = provinceRepository.findByProvinceCode(code);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Set<District> districts = optional.get().getDistricts();

            if (Long.parseLong(id) > 0) {
                List<District> res = new ArrayList<>();
                districts.forEach(d -> {
                    if (Long.parseLong(id) == d.getId()) {
                        res.add(d);
                    }
                });
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            return new ResponseEntity<>(districts, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
