package com.hoangvn.home24h.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.post.BaiDang;

@Repository
public interface IBaiDangRepository extends JpaRepository<BaiDang, Long> {

}
