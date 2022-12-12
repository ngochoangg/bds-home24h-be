package com.hoangvn.home24h.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangvn.home24h.models.post.BaiDang;
import com.hoangvn.home24h.repository.address.IDistrictRepository;
import com.hoangvn.home24h.repository.address.IProvinceRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;

@Service
public class PostService {
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IDistrictRepository districtRepository;

    @Autowired
    IUserRepository userRepository;

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
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(baiDang, BaiDang.class);
    }
}
