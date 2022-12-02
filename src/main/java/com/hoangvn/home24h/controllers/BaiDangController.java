package com.hoangvn.home24h.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.post.BaiDang;
import com.hoangvn.home24h.repository.IBaiDangRepository;
import com.hoangvn.home24h.repository.address.IDistrictRepository;

@RestController
@CrossOrigin
public class BaiDangController {
    @Autowired
    IBaiDangRepository baiDangRepository;
    @Autowired
    IDistrictRepository districtRepository;

    @GetMapping(path = "/posts")
    public ResponseEntity<Object> danhSachBaiDang(@RequestParam(defaultValue = "0", required = false) String p) {
        try {
            Pageable pageWithFiveElements = PageRequest.of(Integer.parseInt(p), 10);
            List<BaiDang> list = new ArrayList<>();
            baiDangRepository.findAll(pageWithFiveElements).forEach(list::add);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/post")
    public ResponseEntity<Object> createPost(@RequestBody BaiDang baiMoi) {
        try {
            baiMoi.setNgayTao(new Date());
            BaiDang saved = baiDangRepository.save(baiMoi);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
