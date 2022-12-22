package com.hoangvn.home24h.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangvn.home24h.models.post.BaiDang;
import com.hoangvn.home24h.repository.address.IDistrictRepository;
import com.hoangvn.home24h.repository.address.IProvinceRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;

@Service
public class BaiDangService {
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IDistrictRepository districtRepository;

    @Autowired
    IUserRepository userRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public BaiDang convertToBaiDang(Map<String, Object> baiDang) {
        baiDang.entrySet().forEach(kv -> {
            if ("tinhThanh".equals(kv.getKey())) {
                baiDang.replace("tinhThanh",
                        provinceRepository.findByProvinceCode(kv.getValue().toString()).get());
            }
            if ("quanHuyen".equals(kv.getKey())) {
                baiDang.replace("quanHuyen",
                        districtRepository.findById(Long.parseLong(kv.getValue().toString())).get());
            }
            if ("user".equals(kv.getKey())) {
                baiDang.replace("user",
                        userRepository.findByUsername(kv.getValue().toString()).get());
            }
        });
        return MAPPER.convertValue(baiDang, BaiDang.class);
    }

    public BaiDang updateBaiDang(Map<String, Object> baiDang, BaiDang baiDangOld) {
        Map<String, Object> map = MAPPER.convertValue(baiDangOld, new TypeReference<Map<String, Object>>() {
        });
        baiDang.entrySet().forEach(kv -> {
            map.put(kv.getKey(), kv.getValue());
            if ("tinhThanh".equals(kv.getKey())) {
                map.replace("tinhThanh",
                        provinceRepository.findByProvinceCode(kv.getValue().toString()).get());
            }
            if ("quanHuyen".equals(kv.getKey())) {
                map.replace("quanHuyen",
                        districtRepository.findById(Long.parseLong(kv.getValue().toString())).get());
            }
            if ("user".equals(kv.getKey())) {
                map.replace("user",
                        userRepository.findByUsername(kv.getValue().toString()).get());
            }
        });
        return MAPPER.convertValue(map, new TypeReference<BaiDang>() {
        });
    }
}
