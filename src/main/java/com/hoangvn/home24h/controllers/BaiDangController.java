package com.hoangvn.home24h.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.post.BaiDang;
import com.hoangvn.home24h.repository.IBaiDangRepository;
import com.hoangvn.home24h.repository.address.IDistrictRepository;
import com.hoangvn.home24h.repository.address.IProvinceRepository;
import com.hoangvn.home24h.repository.address.IWardRepository;
import com.hoangvn.home24h.services.PostService;

@RestController
@CrossOrigin
public class BaiDangController {
    @Autowired
    IBaiDangRepository baiDangRepository;
    @Autowired
    IDistrictRepository districtRepository;
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IWardRepository wardRepository;
    @Autowired
    PostService postService;

    @GetMapping(path = "/posts")
    public ResponseEntity<Object> danhSachBaiDang(@RequestParam(defaultValue = "0", required = false) String p,
            @RequestParam(defaultValue = "10", required = false) String s) {
        try {
            Pageable pageWithTen = PageRequest.of(Integer.parseInt(p), Integer.parseInt(s));
            List<BaiDang> list = new ArrayList<>();
            baiDangRepository.findAll(pageWithTen).forEach(list::add);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/post")
    public ResponseEntity<Object> createPost(@RequestBody Map<String, Object> baiMoi) {
        try {
            BaiDang saved = postService.convertToBaiDang(baiMoi);
            saved.setNgayTao(new Date());
            return new ResponseEntity<>(baiDangRepository.save(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping(path = "/post/lts")
    public ResponseEntity<Object> getLastestPost(@RequestParam(required = false, defaultValue = "6") String post) {
        try {
            return new ResponseEntity<>(baiDangRepository.findByNgayTao(Long.parseLong(post)), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
