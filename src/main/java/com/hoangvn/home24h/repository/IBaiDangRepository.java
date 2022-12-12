package com.hoangvn.home24h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangvn.home24h.models.post.BaiDang;

@Repository
public interface IBaiDangRepository extends JpaRepository<BaiDang, Long> {
    public Optional<BaiDang> findByLoaiNhaDat(String loaiNhaDat);

    @Query(value = "SELECT * FROM posts WHERE da_ban_chua = false ORDER BY ngay_tao DESC LIMIT :soBai", nativeQuery = true)
    List<BaiDang> findByNgayTao(@Param("soBai") Long soBai);

    @Query(value = "SELECT * FROM posts WHERE user_id = :userId", nativeQuery = true)
    List<BaiDang> findByUserId(@Param("userId") Long userId);
}
